package com.soar.timetoeat.client.portal;

import com.soar.timetoeat.client.portal.dao.AuthClient;
import com.soar.timetoeat.client.portal.dao.RestaurantClient;
import com.soar.timetoeat.util.domain.auth.UserRole;
import com.soar.timetoeat.util.domain.restaurant.Restaurant;
import com.soar.timetoeat.util.domain.restaurant.RestaurantWithMenu;
import com.soar.timetoeat.util.params.auth.CreateUserParams.CreateUserParamsBuilder;
import com.soar.timetoeat.util.params.auth.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableDiscoveryClient
public class ClientPortal extends JPanel implements ActionListener {

    private static JFrame frame;
    private final AuthClient authClient;
    private final RestaurantClient restaurantClient;

    private static int frameWidth = 650;
    private static int frameHeight = 600;
    private static String token = null;
    private static Set<Restaurant> restaurants;
    private static String selectedRestaurantName;
    private static RestaurantWithMenu selectedRestaurant;

    private JTabbedPane tabbedPane;

    //Login fields
    private JTextField login_usernameText;
    private JPasswordField login_passwordText;

    //register fields
    private JTextField register_emailText;
    private JTextField register_usernameText;
    private JPasswordField register_passwordText;

    //restaurant fields
    private JPanel homePanel;
    private JList<String> restaurantList;
    private JTextField restaurant_nameText;
    private JTextField restaurant_addressText;
    private static boolean selectedAtLeastOneRestaurant = false;

    @Autowired
    public ClientPortal(final AuthClient authClient,
                        final RestaurantClient restaurantClient) {
        this.authClient = authClient;
        this.restaurantClient = restaurantClient;
        initWindow();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ClientPortal.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            frame = new JFrame("Client Portal");
            ClientPortal ex = ctx.getBean(ClientPortal.class);
            frame.getContentPane().add(ex, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(frameWidth, frameHeight);
            frame.setVisible(true);
        });
    }

    private void initWindow() {
        tabbedPane = new JTabbedPane();

        //login
        JPanel loginPanel = new JPanel();
        placeLoginComponents(loginPanel);
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.setSelectedIndex(0);

        //register
        JPanel registerPanel = new JPanel();
        placeRegisterComponents(registerPanel);
        tabbedPane.addTab("Register", registerPanel);

        //home
        homePanel = new JPanel();
        tabbedPane.addTab("Home", homePanel);
        placeHomeComponents(homePanel);
        //initially, home won't be accessible
        tabbedPane.setEnabledAt(2, false);

        //Add tabbed pane to panel
        setLayout(new GridLayout(1, 1));
        add(tabbedPane);
    }

    private void placeLoginComponents(final JPanel panel) {

        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(30 + (frameWidth >> 2), 10 + (frameHeight >> 2), 80, 25);
        panel.add(userLabel);

        login_usernameText = new JTextField(30);
        login_usernameText.setBounds(120 + (frameWidth >> 2), 10 + (frameHeight >> 2), 160, 25);
        panel.add(login_usernameText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30 + (frameWidth >> 2), 40 + (frameHeight >> 2), 80, 25);
        panel.add(passwordLabel);

        login_passwordText = new JPasswordField(30);
        login_passwordText.setBounds(120 + (frameWidth >> 2), 40 + (frameHeight >> 2), 160, 25);
        panel.add(login_passwordText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(200 + (frameWidth >> 2), 80 + (frameHeight >> 2), 80, 25);
        loginButton.addActionListener(this);
        panel.add(loginButton);
    }

    private void placeRegisterComponents(final JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(30 + (frameWidth >> 2), 10 + (frameHeight >> 2), 80, 25);
        panel.add(userLabel);

        register_usernameText = new JTextField(30);
        register_usernameText.setBounds(120 + (frameWidth >> 2), 10 + (frameHeight >> 2), 160, 25);
        panel.add(register_usernameText);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(30 + (frameWidth >> 2), 40 + (frameHeight >> 2), 80, 25);
        panel.add(emailLabel);

        register_emailText = new JTextField(30);
        register_emailText.setBounds(120 + (frameWidth >> 2), 40 + (frameHeight >> 2), 160, 25);
        panel.add(register_emailText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30 + (frameWidth >> 2), 70 + (frameHeight >> 2), 80, 25);
        panel.add(passwordLabel);

        register_passwordText = new JPasswordField(30);
        register_passwordText.setBounds(120 + (frameWidth >> 2), 70 + (frameHeight >> 2), 160, 25);
        panel.add(register_passwordText);

        JButton registerButton = new JButton("register");
        registerButton.setBounds(200 + (frameWidth >> 2), 110 + (frameHeight >> 2), 80, 25);
        registerButton.addActionListener(this);
        panel.add(registerButton);
    }

    private void placeHomeComponents(final JPanel panel) {
        panel.setLayout(null);

        JLabel detailsLabel = new JLabel("Pick a Restaurant");
        detailsLabel.setBounds(10, 10, 200, 30);
        detailsLabel.setFont(new Font(detailsLabel.getName(), Font.BOLD, 20));
        panel.add(detailsLabel);

    }

    private void updateHomeFields(final JPanel panel) {
        //refresh restaurant list
        restaurants = restaurantClient.getAllRestaurants();
        final String[] restaurantNames = new String[restaurants.size()];
        restaurants.stream()
                .map(Restaurant::getName)
                .collect(Collectors.toList())
                .toArray(restaurantNames);
        restaurantList = new JList<>(restaurantNames);
        restaurantList.setBounds(10, 40, 200, 300);
        restaurantList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                selectedRestaurantName = restaurantList.getSelectedValue();
                updateSelectedRestaurant(panel);
                selectedAtLeastOneRestaurant = true;
            }
        });
        panel.add(restaurantList);
    }

    private void updateSelectedRestaurant(final JPanel panel) {
        if (!Objects.isNull(selectedRestaurantName)) {
            final ResponseEntity<RestaurantWithMenu> restaurantResponse = restaurantClient.getRestaurant(selectedRestaurantName);
            if (restaurantResponse.getStatusCode() == HttpStatus.OK) {
                selectedRestaurant = restaurantResponse.getBody();

                if (!selectedAtLeastOneRestaurant) {
                    //add restaurant details
                    JLabel detailsLabel = new JLabel("Selected Restaurant");
                    detailsLabel.setBounds(220, 10, 200, 30);
                    detailsLabel.setFont(new Font(detailsLabel.getName(), Font.BOLD, 20));
                    panel.add(detailsLabel);

                    JLabel nameLabel = new JLabel("Name");
                    nameLabel.setBounds(220, 40, 80, 25);
                    panel.add(nameLabel);

                    restaurant_nameText = new JTextField(30);
                    restaurant_nameText.setBounds(310, 40, 160, 25);
                    restaurant_nameText.setEditable(false);
                    panel.add(restaurant_nameText);

                    JLabel addressLabel = new JLabel("Address");
                    addressLabel.setBounds(220, 70, 80, 25);
                    panel.add(addressLabel);

                    restaurant_addressText = new JTextField(30);
                    restaurant_addressText.setBounds(310, 70, 160, 25);
                    restaurant_addressText.setEditable(false);
                    panel.add(restaurant_addressText);

                    panel.revalidate();
                    panel.repaint();
                }
                restaurant_nameText.setText(selectedRestaurant.getName());
                restaurant_addressText.setText(selectedRestaurant.getAddress());
            }
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        switch (e.getActionCommand()) {
            case "login":
                login();
                break;
            case "register":
                register();
                break;
            default:
                JOptionPane.showMessageDialog(null, "A confused button click. What Do I do with " + e.getActionCommand() + "?");
                break;
        }
        frame.validate();
    }


    /**
     * Perform a login
     */
    private void login() {
        final ResponseEntity<Void> loginResponse = authClient.login(new LoginRequest(login_usernameText.getText(), new String(login_passwordText.getPassword())));
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
            //store token locally for future http calls
            token = loginResponse.getHeaders().get("Authorization").get(0);
            updateHomeFields(homePanel);
            //clear fields
            login_usernameText.setText("");
            login_passwordText.setText("");

            //change tab states
            tabbedPane.setEnabledAt(0, false);
            tabbedPane.setEnabledAt(1, false);
            tabbedPane.setEnabledAt(2, true);

            //switch to home tab
            tabbedPane.setSelectedIndex(2);
        } else {
            JOptionPane.showMessageDialog(null, "Wrong Username and Password");
        }
    }

    /**
     * Perform a register user
     */
    private void register() {
        final ResponseEntity<Void> newUser = authClient.register(CreateUserParamsBuilder.aCreateUserParams()
                .withEmail(register_emailText.getText())
                .withPassword(new String(register_passwordText.getPassword()))
                .withUsername(register_usernameText.getText())
                .withRole(UserRole.CLIENT)
                .build());
        if (newUser.getStatusCode() == HttpStatus.OK) {
            //clear fields
            register_emailText.setText("");
            register_passwordText.setText("");
            register_usernameText.setText("");

            //navigate to login
            tabbedPane.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(null, "Oops, Something went wrong!");
        }
    }

}