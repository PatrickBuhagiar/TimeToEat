package com.soar.timetoeat.restaurant.portal;

import com.soar.timetoeat.restaurant.portal.dao.AuthClient;
import com.soar.timetoeat.restaurant.portal.dao.RestaurantClient;
import com.soar.timetoeat.restaurant.portal.domain.LoginRequest;
import com.soar.timetoeat.util.domain.menu.Menu;
import com.soar.timetoeat.util.domain.restaurant.Restaurant;
import com.soar.timetoeat.util.domain.auth.UserRole;
import com.soar.timetoeat.util.params.restaurant.CreateRestaurantParams.CreateRestaurantParamsBuilder;
import com.soar.timetoeat.util.params.auth.CreateUserParams.CreateUserParamsBuilder;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableDiscoveryClient
public class RestaurantPortal extends JPanel implements ActionListener, ChangeListener {

    private static JFrame frame;
    private final AuthClient authClient;
    private final RestaurantClient restaurantClient;

    private static int frameWidth = 1000;
    private static int frameHeight = 600;
    private static String token = null;
    private static Restaurant currentRestaurant;
    private static Menu currentMenu;

    private JTabbedPane tabbedPane;

    //Login fields
    private JTextField login_usernameText;
    private JPasswordField login_passwordText;

    //register fields
    private JTextField register_emailText;
    private JTextField register_usernameText;
    private JPasswordField register_passwordText;

    //restaurant fields
    private JPanel restaurantPanel;
    private JTextField restaurant_nameText;
    private JTextField restaurant_addressText;
    private JButton createRestaurantButton;


    @Autowired
    public RestaurantPortal(final AuthClient authClient,
                            final RestaurantClient restaurantClient) {
        this.authClient = authClient;
        this.restaurantClient = restaurantClient;
        initWindow();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(RestaurantPortal.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            frame = new JFrame("Restaurant Portal");
            RestaurantPortal ex = ctx.getBean(RestaurantPortal.class);
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
        restaurantPanel = new JPanel();
        placeRestaurantComponents(restaurantPanel);
        tabbedPane.addTab("Home", restaurantPanel);
        //initially, home won't be accessible
        tabbedPane.setEnabledAt(2, false);

        //Add tabbed pane to panel
        setLayout(new GridLayout(1, 1));
        add(tabbedPane);
    }

    private void placeLoginComponents(final JPanel panel) {

        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(100 + (frameWidth >> 2), 10 + (frameHeight >> 2), 80, 25);
        panel.add(userLabel);

        login_usernameText = new JTextField(30);
        login_usernameText.setBounds(190 + (frameWidth >> 2), 10 + (frameHeight >> 2), 160, 25);
        panel.add(login_usernameText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(100 + (frameWidth >> 2), 40 + (frameHeight >> 2), 80, 25);
        panel.add(passwordLabel);

        login_passwordText = new JPasswordField(30);
        login_passwordText.setBounds(190 + (frameWidth >> 2), 40 + (frameHeight >> 2), 160, 25);
        panel.add(login_passwordText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(270 + (frameWidth >> 2), 80 + (frameHeight >> 2), 80, 25);
        loginButton.addActionListener(this);
        panel.add(loginButton);


    }

    private void placeRegisterComponents(final JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(100 + (frameWidth >> 2), 10 + (frameHeight >> 2), 80, 25);
        panel.add(userLabel);

        register_usernameText = new JTextField(30);
        register_usernameText.setBounds(190 + (frameWidth >> 2), 10 + (frameHeight >> 2), 160, 25);
        panel.add(register_usernameText);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(100 + (frameWidth >> 2), 40 + (frameHeight >> 2), 80, 25);
        panel.add(emailLabel);

        register_emailText = new JTextField(30);
        register_emailText.setBounds(190 + (frameWidth >> 2), 40 + (frameHeight >> 2), 160, 25);
        panel.add(register_emailText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(100 + (frameWidth >> 2), 70 + (frameHeight >> 2), 80, 25);
        panel.add(passwordLabel);

        register_passwordText = new JPasswordField(30);
        register_passwordText.setBounds(190 + (frameWidth >> 2), 70 + (frameHeight >> 2), 160, 25);
        panel.add(register_passwordText);

        JButton registerButton = new JButton("register");
        registerButton.setBounds(270 + (frameWidth >> 2), 110 + (frameHeight >> 2), 80, 25);
        registerButton.addActionListener(this);
        panel.add(registerButton);
    }

    private void placeRestaurantComponents(final JPanel panel) {
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Restaurant");
        nameLabel.setBounds(100 + (frameWidth >> 2), 10 + (frameHeight >> 2), 80, 25);
        panel.add(nameLabel);

        restaurant_nameText = new JTextField(30);
        restaurant_nameText.setBounds(190 + (frameWidth >> 2), 10 + (frameHeight >> 2), 160, 25);
        panel.add(restaurant_nameText);

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(100 + (frameWidth >> 2), 40 + (frameHeight >> 2), 80, 25);
        panel.add(addressLabel);

        restaurant_addressText = new JTextField(30);
        restaurant_addressText.setBounds(190 + (frameWidth >> 2), 40 + (frameHeight >> 2), 160, 25);
        panel.add(restaurant_addressText);

        updateRestaurantFields(panel);
    }

    private void updateRestaurantFields(final JPanel panel) {
        if (Objects.isNull(currentRestaurant)) {
            createRestaurantButton = new JButton("create restaurant");
            createRestaurantButton.setBounds(200 + (frameWidth >> 2), 80 + (frameHeight >> 2), 150, 25);
            createRestaurantButton.addActionListener(this);
            panel.add(createRestaurantButton);
        } else {
            restaurant_nameText.setText(currentRestaurant.getName());
            restaurant_nameText.setEditable(false);
            restaurant_addressText.setText(currentRestaurant.getAddress());
            restaurant_addressText.setEditable(false);
            panel.remove(createRestaurantButton);
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
            case "create restaurant":
                createRestaurant();
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
            //get Restaurant
            currentRestaurant = restaurantClient.getRestaurantByOwner(token);
            updateRestaurantFields(restaurantPanel);

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
                .withRole(UserRole.RESTAURANT)
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

    /**
     * Create a restaurant
     */
    private void createRestaurant() {
        if (Objects.isNull(currentRestaurant)) {
            final ResponseEntity<Restaurant> restaurantResponse = restaurantClient.createRestaurant(token, CreateRestaurantParamsBuilder.aCreateRestaurantParams()
                    .withAddress(restaurant_addressText.getText())
                    .withName(restaurant_nameText.getText())
                    .build());

            if (restaurantResponse.getStatusCode() == HttpStatus.CREATED) {
                currentRestaurant = restaurantResponse.getBody();
                updateRestaurantFields(restaurantPanel);
            } else {
                JOptionPane.showMessageDialog(null, "failed to create Restaurant!");
            }
        }
    }

    @Override
    public void stateChanged(final ChangeEvent e) {

    }
}
