package com.soar.timetoeat.client.portal;

import com.soar.timetoeat.client.portal.dao.AuthClient;
import com.soar.timetoeat.client.portal.dao.OrderClient;
import com.soar.timetoeat.client.portal.dao.RestaurantClient;
import com.soar.timetoeat.util.domain.auth.UserRole;
import com.soar.timetoeat.util.domain.order.RestaurantOrder;
import com.soar.timetoeat.util.domain.restaurant.Restaurant;
import com.soar.timetoeat.util.domain.restaurant.RestaurantWithMenu;
import com.soar.timetoeat.util.faults.ClientException;
import com.soar.timetoeat.util.params.auth.CreateUserParams.CreateUserParamsBuilder;
import com.soar.timetoeat.util.params.auth.LoginRequest;
import com.soar.timetoeat.util.params.order.CreateOrderItemParams;
import com.soar.timetoeat.util.params.order.CreateOrderItemParams.CreateOrderItemParamsBuilder;
import com.soar.timetoeat.util.params.order.CreateOrderParams;
import com.soar.timetoeat.util.params.order.CreateOrderParams.CreateOrderParamsBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.jms.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableAutoConfiguration(exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableFeignClients
@EnableEurekaClient
@EnableDiscoveryClient
public class ClientPortal extends JPanel implements ActionListener {

    private static JFrame frame;
    private final AuthClient authClient;
    private final RestaurantClient restaurantClient;
    private final OrderClient orderClient;

    private static int frameWidth = 650;
    private static int frameHeight = 600;
    private String token = null;
    private Set<Restaurant> restaurants;
    private List<RestaurantOrder> orderHistory;
    private String selectedRestaurantName;
    private RestaurantWithMenu selectedRestaurant;

    private JTabbedPane tabbedPane;

    //Login fields
    private JTextField login_usernameText;
    private JPasswordField login_passwordText;

    //register fields
    private JTextField register_emailText;
    private JTextField register_usernameText;
    private JPasswordField register_passwordText;
    private JTextField register_fullNameText;

    //restaurant fields
    private JPanel homePanel;
    private JList<String> restaurantList;
    private JTextField restaurant_nameText;
    private JTextField restaurant_addressText;
    private JTextField restaurant_deliveryAddressText;
    private JTextField restaurant_cardNumberText;
    private JTextField restaurant_cvvText;
    private boolean selectedAtLeastOneRestaurant = false;
    private DefaultTableModel restaurant_dtm;
    private JButton createOrderButton;
    private RestaurantOrder currentOrder;

    //order fields
    private JPanel orderPanel;
    private DefaultTableModel order_dtm;
    private Session session;
    private Topic topic;

    @Autowired
    public ClientPortal(final AuthClient authClient,
                        final RestaurantClient restaurantClient,
                        final OrderClient orderClient) throws JMSException {
        this.authClient = authClient;
        this.restaurantClient = restaurantClient;
        this.orderClient = orderClient;
        initConnection();
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

    private void initConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:3000");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * Create a Queue for receiving orders
     */
    private void initialiseTopic() {
        if (Objects.isNull(topic)) {
            try {
                topic = session.createTopic("cli-" + login_usernameText.getText());
                final MessageConsumer consumer = session.createConsumer(topic);
                consumer.setMessageListener(new MessageListener());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
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

        //order
        orderPanel = new JPanel();
        tabbedPane.addTab("Order History", orderPanel);
        placeOrderComponents(orderPanel);
        tabbedPane.setEnabledAt(3, false);

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

        JLabel fullNameLabel = new JLabel("Full Name");
        fullNameLabel.setBounds(30 + (frameWidth >> 2), 40 + (frameHeight >> 2), 80, 25);
        panel.add(fullNameLabel);

        register_fullNameText = new JTextField(30);
        register_fullNameText.setBounds(120 + (frameWidth >> 2), 40 + (frameHeight >> 2), 160, 25);
        panel.add(register_fullNameText);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(30 + (frameWidth >> 2), 70 + (frameHeight >> 2), 80, 25);
        panel.add(emailLabel);

        register_emailText = new JTextField(30);
        register_emailText.setBounds(120 + (frameWidth >> 2), 70 + (frameHeight >> 2), 160, 25);
        panel.add(register_emailText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30 + (frameWidth >> 2), 100 + (frameHeight >> 2), 80, 25);
        panel.add(passwordLabel);

        register_passwordText = new JPasswordField(30);
        register_passwordText.setBounds(120 + (frameWidth >> 2), 100 + (frameHeight >> 2), 160, 25);
        panel.add(register_passwordText);

        JButton registerButton = new JButton("register");
        registerButton.setBounds(200 + (frameWidth >> 2), 140 + (frameHeight >> 2), 80, 25);
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

    private void placeOrderComponents(final JPanel panel) {
        panel.setLayout(null);

        JLabel detailsLabel = new JLabel("Order History");
        detailsLabel.setBounds(10, 10, 200, 30);
        detailsLabel.setFont(new Font(detailsLabel.getName(), Font.BOLD, 20));
        panel.add(detailsLabel);

        //Add table
        String[] columnNames = new String[]{"Restaurant", "Items", "Total Price", "Status", "Expected Arrival Time"};
        JTable orderHistoryTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        order_dtm = new DefaultTableModel(0, 0);
        order_dtm.setColumnIdentifiers(columnNames);
        orderHistoryTable.setModel(order_dtm);
        final JScrollPane jScrollPane = new JScrollPane(orderHistoryTable);
        jScrollPane.setVisible(true);
        jScrollPane.setBounds(10, 40, 600, 310);
        panel.add(jScrollPane);

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
        restaurantList.setBounds(10, 40, 200, 400);
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
            final ResponseEntity<RestaurantWithMenu> restaurantResponse;
            try {
                restaurantResponse = restaurantClient.getRestaurant(selectedRestaurantName);

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

                        JLabel menuDetails = new JLabel("Order Menu");
                        menuDetails.setBounds(220, 100, 200, 30);
                        menuDetails.setFont(new Font(menuDetails.getName(), Font.BOLD, 20));
                        panel.add(menuDetails);

                        //Add table
                        String[] columnNames = new String[]{"Name", "Description", "UnitPrice", "Quantity"};
                        JTable menuTable = new JTable() {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return column == 3;
                            }
                        };
                        restaurant_dtm = new DefaultTableModel(0, 0);
                        restaurant_dtm.setColumnIdentifiers(columnNames);
                        menuTable.setModel(restaurant_dtm);
                        final JScrollPane jScrollPane = new JScrollPane(menuTable);
                        jScrollPane.setVisible(true);
                        jScrollPane.setBounds(220, 130, 380, 310);
                        panel.add(jScrollPane);

                        //create order button
                        createOrderButton = new JButton("create order");
                        createOrderButton.setBounds(480, 480, 120, 25);
                        createOrderButton.addActionListener(this);
                        panel.add(createOrderButton);

                        //Add Address field
                        JLabel deliveryAddressLabel = new JLabel("Delivery Address");
                        deliveryAddressLabel.setBounds(10, 450, 130, 25);
                        panel.add(deliveryAddressLabel);

                        restaurant_deliveryAddressText = new JTextField(30);
                        restaurant_deliveryAddressText.setBounds(150, 450, 160, 25);
                        panel.add(restaurant_deliveryAddressText);

                        //Add Card field
                        JLabel cardNumberLabel = new JLabel("Card Number");
                        cardNumberLabel.setBounds(10, 480, 130, 25);
                        panel.add(cardNumberLabel);

                        NumberFormat format = NumberFormat.getInstance();
                        format.setGroupingUsed(false);
                        NumberFormatter formatter = new NumberFormatter(format);
                        formatter.setValueClass(Long.class);
                        formatter.setAllowsInvalid(false);
                        formatter.setCommitsOnValidEdit(true);
                        restaurant_cardNumberText = new JFormattedTextField(formatter);

                        restaurant_cardNumberText.setBounds(150, 480, 160, 25);
                        panel.add(restaurant_cardNumberText);

                        //Add CVV field
                        JLabel cvvLabel = new JLabel("CVV");
                        cvvLabel.setBounds(320, 480, 30, 25);
                        panel.add(cvvLabel);

                        NumberFormat format2 = NumberFormat.getInstance();
                        format2.setGroupingUsed(false);
                        NumberFormatter formatter2 = new NumberFormatter(format2);
                        formatter2.setValueClass(Integer.class);
                        formatter2.setMaximum(999);
                        formatter2.setAllowsInvalid(false);
                        formatter2.setCommitsOnValidEdit(true);
                        restaurant_cvvText = new JFormattedTextField(formatter2);
                        restaurant_cvvText.setBounds(360, 480, 50, 25);
                        panel.add(restaurant_cvvText);

                        panel.revalidate();
                        panel.repaint();
                    }
                    populateMenuTableFromCurrentMenu();
                    restaurant_nameText.setText(selectedRestaurant.getName());
                    restaurant_addressText.setText(selectedRestaurant.getAddress());
                }
            } catch (ClientException e) {
                showErrorDialogue(e);
            }
        }
    }

    private void populateMenuTableFromCurrentMenu() {
        restaurant_dtm.setRowCount(0);
        selectedRestaurant.getMenu().getItems()
                .forEach(item -> restaurant_dtm.addRow(new Object[]{item.getName(), item.getDescription(), item.getUnitPrice(), 0}));
    }

    private void populateOrderHistoryTableFromOrderHistory() {
        order_dtm.setRowCount(0);
        orderHistory.forEach(order -> {
            String expectedDeliveryTimeString = "";
            final Long expectedDeliveryTime = order.getExpectedDeliveryTime();
            if (!Objects.isNull(expectedDeliveryTime)) {
                final Timestamp timestamp = new Timestamp(expectedDeliveryTime);
                Date expectedDate = new Date(timestamp.getTime());
                expectedDeliveryTimeString = expectedDate.toString();
            }
            order_dtm.addRow(new Object[]{order.getRestaurantName(), order.itemsAsString(), order.getTotalPrice(), order.getState(), expectedDeliveryTimeString});
        });
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
            case "create order":
                createOrder();
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
        final ResponseEntity<Void> loginResponse;
        try {
            loginResponse = authClient.login(new LoginRequest(login_usernameText.getText(), new String(login_passwordText.getPassword())));
            if (loginResponse.getStatusCode() == HttpStatus.OK) {
                //store token locally for future http calls
                token = loginResponse.getHeaders().get("Authorization").get(0);
                updateOrderHistory();
                initialiseTopic();
                updateHomeFields(homePanel);
                //clear fields
                login_usernameText.setText("");
                login_passwordText.setText("");

                //change tab states
                tabbedPane.setEnabledAt(0, false);
                tabbedPane.setEnabledAt(1, false);
                tabbedPane.setEnabledAt(2, true);
                tabbedPane.setEnabledAt(3, true);

                //switch to home tab
                tabbedPane.setSelectedIndex(2);
            }
        } catch (ClientException e) {
            showErrorDialogue(e);
        }
    }

    /**
     * Perform a register user
     */
    private void register() {
        final ResponseEntity<Void> newUser;
        try {
            newUser = authClient.register(CreateUserParamsBuilder.aCreateUserParams()
                    .withEmail(register_emailText.getText())
                    .withPassword(new String(register_passwordText.getPassword()))
                    .withUsername(register_usernameText.getText())
                    .withFullName(register_fullNameText.getText())
                    .withRole(UserRole.CLIENT)
                    .build());

            if (newUser.getStatusCode() == HttpStatus.OK) {
                //clear fields
                register_emailText.setText("");
                register_passwordText.setText("");
                register_usernameText.setText("");

                //navigate to login
                tabbedPane.setSelectedIndex(0);
            }
        } catch (ClientException e) {
            showErrorDialogue(e);
        }
    }

    /**
     * Create an order
     */
    private void createOrder() {
        final CreateOrderParams createOrderParams = extractOrderParamsFromTable();
        final ResponseEntity<RestaurantOrder> orderResponse;
        try {
            orderResponse = orderClient.createOrder(token, selectedRestaurant.getName(), createOrderParams);
            if (orderResponse.getStatusCode() == HttpStatus.CREATED) {
                currentOrder = orderResponse.getBody();
                updateOrderHistory();
                tabbedPane.setSelectedIndex(3);
            }
        } catch (ClientException e) {
            showErrorDialogue(e);
        }
    }

    private void updateOrderHistory() {
        try {
            orderHistory = orderClient.getClientOrders(token).stream().sorted(Comparator.comparingLong(RestaurantOrder::getId).reversed()).collect(Collectors.toList());
        } catch (ClientException e) {
            showErrorDialogue(e);
        }
        populateOrderHistoryTableFromOrderHistory();
    }

    private CreateOrderParams extractOrderParamsFromTable() {
        Set<CreateOrderItemParams> itemParams = new HashSet<>();
        final Object[][] tableData = new Object[restaurant_dtm.getRowCount()][restaurant_dtm.getColumnCount()];
        for (int i = 0; i < restaurant_dtm.getRowCount(); i++) {
            for (int j = 0; j < restaurant_dtm.getColumnCount(); j++) {
                tableData[i][j] = restaurant_dtm.getValueAt(i, j);
            }

            final CreateOrderItemParams params = CreateOrderItemParamsBuilder.aCreateOrderItemParams()
                    .withName(tableData[i][0].toString())
                    .withUnitPrice(Double.valueOf(tableData[i][2].toString()))
                    .withQuantity(Integer.valueOf(tableData[i][3].toString()))
                    .build();
            if (params.getQuantity() != 0) {
                itemParams.add(params);
            }
        }
        final String cardNumber = restaurant_cardNumberText.getText();
        final String cvv = restaurant_cvvText.getText();
        return CreateOrderParamsBuilder.aCreateOrderParams()
                .withItems(itemParams)
                .withCardNumber(cardNumber.isEmpty() ? 0L : Long.valueOf(cardNumber))
                .withCvv(cvv.isEmpty() ? 0 : Integer.valueOf(cvv))
                .withDeliveryAddress(restaurant_deliveryAddressText.getText())
                .build();
    }

    private void showErrorDialogue(final ClientException e) {
        JOptionPane.showMessageDialog(frame, e.getExceptionResponse().getDescription());
    }

    class MessageListener implements javax.jms.MessageListener {

        @Override
        public void onMessage(final Message message) {
            try {
                TextMessage textMessage = (TextMessage) message;
                updateOrderHistory();
                JOptionPane.showMessageDialog(frame, textMessage.getText());
                tabbedPane.setSelectedIndex(3);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
