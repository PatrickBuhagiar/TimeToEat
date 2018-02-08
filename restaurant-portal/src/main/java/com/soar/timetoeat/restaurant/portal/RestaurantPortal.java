package com.soar.timetoeat.restaurant.portal;

import com.soar.timetoeat.restaurant.portal.dao.AuthClient;
import com.soar.timetoeat.restaurant.portal.dao.MenuClient;
import com.soar.timetoeat.restaurant.portal.dao.RestaurantClient;
import com.soar.timetoeat.util.domain.auth.UserRole;
import com.soar.timetoeat.util.domain.menu.Menu;
import com.soar.timetoeat.util.domain.restaurant.Restaurant;
import com.soar.timetoeat.util.params.auth.CreateUserParams.CreateUserParamsBuilder;
import com.soar.timetoeat.util.params.auth.LoginRequest;
import com.soar.timetoeat.util.params.menu.CreateItemParams;
import com.soar.timetoeat.util.params.menu.CreateItemParams.CreateItemParamsBuilder;
import com.soar.timetoeat.util.params.menu.CreateMenuParams;
import com.soar.timetoeat.util.params.menu.CreateMenuParams.CreateMenuParamsBuilder;
import com.soar.timetoeat.util.params.restaurant.CreateRestaurantParams.CreateRestaurantParamsBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.jms.*;
import javax.jms.Queue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableDiscoveryClient
public class RestaurantPortal extends JPanel implements ActionListener {

    private Session session;
    private final AuthClient authClient;
    private final RestaurantClient restaurantClient;
    private final MenuClient menuClient;

    private static int frameWidth = 650;
    private static int frameHeight = 600;
    private String token = null;
    private Restaurant currentRestaurant;
    private Menu currentMenu;

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

    //menu fields
    private DefaultTableModel dtm;
    private JTextField menu_nameText;
    private JTextField menu_descriptionText;
    private JFormattedTextField unitPriceText;
    private JButton addToMenuButton;
    private JLabel addToMenuLabel;
    private JLabel menuItemLabel;
    private JLabel menuDescriptionLabel;
    private JLabel unitPriceLabel;
    private Queue queue;

    @Autowired
    public RestaurantPortal(final AuthClient authClient,
                            final RestaurantClient restaurantClient,
                            final MenuClient menuClient) throws JMSException {
        this.authClient = authClient;
        this.restaurantClient = restaurantClient;
        this.menuClient = menuClient;
        initConnection();
        initWindow();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(RestaurantPortal.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Restaurant Portal");
            RestaurantPortal ex = ctx.getBean(RestaurantPortal.class);
            frame.getContentPane().add(ex, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(frameWidth, frameHeight);
            frame.setVisible(true);
        });
    }

    private void initConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:3000");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
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

    private void placeRestaurantComponents(final JPanel panel) {
        panel.setLayout(null);

        //add restaurant details
        JLabel detailsLabel = new JLabel("Restaurant Details");
        detailsLabel.setBounds(10, 10, 200, 30);
        detailsLabel.setFont(new Font(detailsLabel.getName(), Font.BOLD, 20));
        panel.add(detailsLabel);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(10, 40, 80, 25);
        panel.add(nameLabel);

        restaurant_nameText = new JTextField(30);
        restaurant_nameText.setBounds(100, 40, 160, 25);
        panel.add(restaurant_nameText);

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(10, 70, 80, 25);
        panel.add(addressLabel);

        restaurant_addressText = new JTextField(30);
        restaurant_addressText.setBounds(100 , 70 , 160, 25);
        panel.add(restaurant_addressText);

        //Add menu table
        JLabel menuDetails = new JLabel("Menu Items");
        menuDetails.setBounds(280, 10, 200, 30);
        menuDetails.setFont(new Font(menuDetails.getName(), Font.BOLD, 20));
        panel.add(menuDetails);

        String[] columnNames = new String[]{"Name", "Description", "UnitPrice"};
        JTable menuTable = new JTable();
        dtm = new DefaultTableModel(0,0);
        dtm.setColumnIdentifiers(columnNames);
        menuTable.setModel(dtm);
        final JScrollPane menuScrollPane = new JScrollPane(menuTable);
        menuScrollPane.setBounds(280, 40, 300, 400);
        panel.add(menuScrollPane);

        //Add to menu fields
        addToMenuLabel = new JLabel("Add To Menu");
        addToMenuLabel.setBounds(10, 100, 200, 30);
        addToMenuLabel.setFont(new Font(addToMenuLabel.getName(), Font.BOLD, 20));
        panel.add(addToMenuLabel);

        menuItemLabel = new JLabel("Name");
        menuItemLabel.setBounds(10, 130, 80, 25);
        panel.add(menuItemLabel);

        menu_nameText = new JTextField(30);
        menu_nameText.setBounds(100 , 130 , 160, 25);
        panel.add(menu_nameText);

        menuDescriptionLabel = new JLabel("Description");
        menuDescriptionLabel.setBounds(10, 160, 80, 25);
        panel.add(menuDescriptionLabel);

        menu_descriptionText = new JTextField(30);
        menu_descriptionText.setBounds(100 , 160 , 160, 25);
        panel.add(menu_descriptionText);

        unitPriceLabel = new JLabel("Unit Price");
        unitPriceLabel.setBounds(10, 190, 80, 25);
        panel.add(unitPriceLabel);

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.UK);
        format.setMaximumFractionDigits(2);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0.0);
        formatter.setMaximum(1000.0);
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        unitPriceText = new JFormattedTextField(formatter);
        unitPriceText.setBounds(100, 190, 160, 25);
        unitPriceText.setValue(0.0);
        panel.add(unitPriceText);

        addToMenuButton = new JButton("add to menu");
        addToMenuButton.setBounds(100, 220 ,160, 25);
        addToMenuButton.addActionListener(this);
        panel.add(addToMenuButton);

        createRestaurantButton = new JButton("create restaurant and menu");
        createRestaurantButton.setBounds(10, 300, 250, 25);
        createRestaurantButton.addActionListener(this);
        panel.add(createRestaurantButton);

        updateRestaurantFields(panel);
    }

    private void updateRestaurantFields(final JPanel panel) {
        if (!Objects.isNull(currentRestaurant) && !Objects.isNull(currentMenu)) {
            restaurant_nameText.setText(currentRestaurant.getName());
            restaurant_nameText.setEditable(false);
            restaurant_addressText.setText(currentRestaurant.getAddress());
            restaurant_addressText.setEditable(false);

            //remove fields
            panel.remove(createRestaurantButton);
            panel.remove(addToMenuButton);
            panel.remove(addToMenuLabel);
            panel.remove(menuDescriptionLabel);
            panel.remove(menu_descriptionText);
            panel.remove(menuItemLabel);
            panel.remove(menu_nameText);
            panel.remove(unitPriceLabel);
            panel.remove(unitPriceText);
            panel.remove(createRestaurantButton);

            //populate table
            populateMenuTableFromCurrentMenu();
            panel.revalidate();
            panel.repaint();
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
            case "create restaurant and menu":
                createRestaurantAndMenu();
                break;
            case "add to menu":
                addToMenu();
                break;
            default:
                JOptionPane.showMessageDialog(null, "A confused button click. What Do I do with " + e.getActionCommand() + "?");
                break;
        }
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
            if (!Objects.isNull(currentRestaurant)) {
                currentMenu = menuClient.getMenu(currentRestaurant.getId());
                if (Objects.isNull(queue)) {
                    try {
                        queue = session.createQueue("res-" + currentRestaurant.getId());
                        final MessageConsumer consumer = session.createConsumer(queue);
                        consumer.setMessageListener(new MessageListener());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
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
    private void createRestaurantAndMenu() {
        if (Objects.isNull(currentRestaurant)) {
            final ResponseEntity<Restaurant> restaurantResponse = restaurantClient.createRestaurant(token, CreateRestaurantParamsBuilder.aCreateRestaurantParams()
                    .withAddress(restaurant_addressText.getText())
                    .withName(restaurant_nameText.getText())
                    .build());

            if (restaurantResponse.getStatusCode() == HttpStatus.CREATED) {
                final Restaurant restaurant = restaurantResponse.getBody();
                final Menu menu = menuClient.createMenu(token, restaurant.getId(), extractMenuParamsFromTable());

                currentRestaurant = restaurant;
                currentMenu = menu;
                updateRestaurantFields(restaurantPanel);
            } else {
                JOptionPane.showMessageDialog(null, "failed to create Restaurant!");
            }
        }
    }

    private CreateMenuParams extractMenuParamsFromTable(){
        Set<CreateItemParams> itemParams = new HashSet<>();
        final Object[][] tableData = new Object[dtm.getRowCount()][dtm.getColumnCount()];
        for (int i = 0; i < dtm.getRowCount(); i++) {
            for (int j = 0; j < dtm.getColumnCount(); j++) {
                tableData[i][j] = dtm.getValueAt(i,j);
            }

            final CreateItemParams params = CreateItemParamsBuilder.aCreateItemParams()
                    .withName(tableData[i][0].toString())
                    .withDescription(tableData[i][1].toString())
                    .withUnitPrice(Double.valueOf(tableData[i][2].toString()))
                    .build();
            itemParams.add(params);
        }

        return CreateMenuParamsBuilder.aCreateMenuParams()
                .withItems(itemParams)
                .build();
    }

    private void populateMenuTableFromCurrentMenu() {
        dtm.setRowCount(0);
        currentMenu.getItems()
                .forEach(item -> dtm.addRow(new Object[]{item.getName(), item.getDescription(), item.getUnitPrice()}));
    }

    /**
     * Add item to menu table
     */
    private void addToMenu() {
        double price = Double.valueOf(unitPriceText.getValue().toString().replaceAll("[^\\d.]+", ""));
        dtm.addRow(new Object[]{menu_nameText.getText(), menu_descriptionText.getText(), price});
        menu_nameText.setText("");
        menu_descriptionText.setText("");
        unitPriceText.setValue(0.0);
    }

    class MessageListener implements javax.jms.MessageListener {

        @Override
        public void onMessage(final Message message) {
            try {
                MapMessage mapMessage = (MapMessage) message;
                JOptionPane.showMessageDialog(null, "Received new order! " + currentRestaurant.getName() + " has order to " + mapMessage.getString("delivery address"));
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
