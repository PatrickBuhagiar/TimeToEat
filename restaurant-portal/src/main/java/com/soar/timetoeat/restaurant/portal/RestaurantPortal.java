package com.soar.timetoeat.restaurant.portal;

import com.soar.timetoeat.restaurant.portal.dao.AuthClient;
import com.soar.timetoeat.restaurant.portal.dao.MenuClient;
import com.soar.timetoeat.restaurant.portal.dao.OrderClient;
import com.soar.timetoeat.restaurant.portal.dao.RestaurantClient;
import com.soar.timetoeat.util.domain.auth.UserRole;
import com.soar.timetoeat.util.domain.menu.Menu;
import com.soar.timetoeat.util.domain.order.OrderState;
import com.soar.timetoeat.util.domain.order.RestaurantOrder;
import com.soar.timetoeat.util.domain.restaurant.Restaurant;
import com.soar.timetoeat.util.faults.ClientException;
import com.soar.timetoeat.util.params.auth.CreateUserParams.CreateUserParamsBuilder;
import com.soar.timetoeat.util.params.auth.LoginRequest;
import com.soar.timetoeat.util.params.menu.CreateItemParams;
import com.soar.timetoeat.util.params.menu.CreateItemParams.CreateItemParamsBuilder;
import com.soar.timetoeat.util.params.menu.CreateMenuParams;
import com.soar.timetoeat.util.params.menu.CreateMenuParams.CreateMenuParamsBuilder;
import com.soar.timetoeat.util.params.order.UpdateOrderParams.UpdateOrderParamsBuilder;
import com.soar.timetoeat.util.params.restaurant.CreateRestaurantParams.CreateRestaurantParamsBuilder;
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
import javax.jms.Queue;
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

import static javax.swing.JOptionPane.*;

@SpringBootApplication
@EnableAutoConfiguration(exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableFeignClients
@EnableEurekaClient
@EnableDiscoveryClient
public class RestaurantPortal extends JPanel implements ActionListener {

    private static JFrame frame;
    private Session session;
    private final AuthClient authClient;
    private final RestaurantClient restaurantClient;
    private final MenuClient menuClient;
    private final OrderClient orderClient;

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
    private JTextField register_fullNameText;

    //restaurant fields
    private JPanel restaurantPanel;
    private JTextField restaurant_nameText;
    private JTextField restaurant_addressText;
    private JButton createRestaurantButton;

    //menu fields
    private DefaultTableModel menu_dtm;
    private JTextField menu_nameText;
    private JTextField menu_descriptionText;
    private JFormattedTextField unitPriceText;
    private Queue queue;

    //orders
    private static final long ONE_MINUTE_IN_MILLIS = 60000;
    private DefaultTableModel order_dtm;
    private List<RestaurantOrder> orderHistory;
    private RestaurantOrder selectedOrder;
    private JButton updatedOrderButton;
    private String selectedMenuItem;

    @Autowired
    public RestaurantPortal(final AuthClient authClient,
                            final RestaurantClient restaurantClient,
                            final MenuClient menuClient,
                            final OrderClient orderClient) throws JMSException {
        this.authClient = authClient;
        this.restaurantClient = restaurantClient;
        this.menuClient = menuClient;
        this.orderClient = orderClient;
        initConnection();
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

        //order
        final JPanel ordersPanel = new JPanel();
        placeOrderComponents(ordersPanel);
        tabbedPane.addTab("Orders", ordersPanel);
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
        restaurant_addressText.setBounds(100, 70, 160, 25);
        panel.add(restaurant_addressText);

        //Add menu table
        JLabel menuDetails = new JLabel("Menu Items");
        menuDetails.setBounds(280, 10, 200, 30);
        menuDetails.setFont(new Font(menuDetails.getName(), Font.BOLD, 20));
        panel.add(menuDetails);

        String[] columnNames = new String[]{"Name", "Description", "UnitPrice"};
        JTable menuTable = new JTable();
        menu_dtm = new DefaultTableModel(0, 0);
        menu_dtm.setColumnIdentifiers(columnNames);
        menuTable.getSelectionModel().addListSelectionListener(e -> {
            if (menuTable.getSelectedRow() > -1) {
                selectedMenuItem = (String) menuTable.getValueAt(menuTable.getSelectedRow(), 0);
            }
        });
        menuTable.setModel(menu_dtm);
        final JScrollPane menuScrollPane = new JScrollPane(menuTable);
        menuScrollPane.setBounds(280, 40, 300, 400);
        panel.add(menuScrollPane);

        //Add to menu fields
        final JLabel addToMenuLabel = new JLabel("Add To Menu");
        addToMenuLabel.setBounds(10, 100, 200, 30);
        addToMenuLabel.setFont(new Font(addToMenuLabel.getName(), Font.BOLD, 20));
        panel.add(addToMenuLabel);

        final JLabel menuItemLabel = new JLabel("Name");
        menuItemLabel.setBounds(10, 130, 80, 25);
        panel.add(menuItemLabel);

        menu_nameText = new JTextField(30);
        menu_nameText.setBounds(100, 130, 160, 25);
        panel.add(menu_nameText);

        final JLabel menuDescriptionLabel = new JLabel("Description");
        menuDescriptionLabel.setBounds(10, 160, 80, 25);
        panel.add(menuDescriptionLabel);

        menu_descriptionText = new JTextField(30);
        menu_descriptionText.setBounds(100, 160, 160, 25);
        panel.add(menu_descriptionText);

        final JLabel unitPriceLabel = new JLabel("Unit Price");
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

        final JButton addToMenuButton = new JButton("add to menu");
        addToMenuButton.setBounds(100, 220, 160, 25);
        addToMenuButton.addActionListener(this);
        panel.add(addToMenuButton);

        final JButton removeMenuItem = new JButton("remove selected menu item");
        removeMenuItem.setBounds(50, 250, 210, 25);
        removeMenuItem.addActionListener(this);
        panel.add(removeMenuItem);

        createRestaurantButton = new JButton("create restaurant and menu");
        createRestaurantButton.setBounds(10, 300, 250, 25);
        createRestaurantButton.addActionListener(this);
        panel.add(createRestaurantButton);

        updateRestaurantFields(panel);
    }

    private void placeOrderComponents(final JPanel panel) {
        panel.setLayout(null);

        JLabel detailsLabel = new JLabel("Order History");
        detailsLabel.setBounds(10, 10, 200, 30);
        detailsLabel.setFont(new Font(detailsLabel.getName(), Font.BOLD, 20));
        panel.add(detailsLabel);

        //Add table
        String[] columnNames = new String[]{"order id", "Address", "Items", "Total Price", "Expected Arrival Time", "Status"};
        JTable orderHistoryTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        updatedOrderButton = new JButton("update selected order to next state");
        updatedOrderButton.setBounds(10, 350, 600, 25);
        updatedOrderButton.addActionListener(this);
        panel.add(updatedOrderButton);


        order_dtm = new DefaultTableModel(0, 0);
        order_dtm.setColumnIdentifiers(columnNames);
        orderHistoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (orderHistoryTable.getSelectedRow() > -1) {
                final Long selectedOrderId = (Long) orderHistoryTable.getValueAt(orderHistoryTable.getSelectedRow(), 0);
                final Optional<RestaurantOrder> filteredOrder = orderHistory.stream().filter(order -> order.getId() == selectedOrderId).findFirst();
                selectedOrder = filteredOrder.orElse(null);
                if (!Objects.isNull(selectedOrder)) {
                    if (selectedOrder.getState().equals(OrderState.DECLINED) || selectedOrder.getState().equals(OrderState.DELIVERED)) {
                        updatedOrderButton.setEnabled(false);
                    } else {
                        updatedOrderButton.setEnabled(true);
                    }
                }
            } else {
                updatedOrderButton.setEnabled(false);
            }
        });
        orderHistoryTable.setModel(order_dtm);
        final JScrollPane jScrollPane = new JScrollPane(orderHistoryTable);
        jScrollPane.setVisible(true);
        jScrollPane.setBounds(10, 40, 600, 310);
        panel.add(jScrollPane);

    }

    private void updateRestaurantFields(final JPanel panel) {
        if (!Objects.isNull(currentRestaurant) && !Objects.isNull(currentMenu)) {
            restaurant_nameText.setText(currentRestaurant.getName());
            restaurant_nameText.setEditable(false);
            restaurant_addressText.setText(currentRestaurant.getAddress());
            restaurant_addressText.setEditable(false);

            //remove fields
            createRestaurantButton.setText("update menu");

            //populate table
            populateMenuTableFromCurrentMenu();
            panel.revalidate();
            panel.repaint();
        } else {

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
            case "update menu":
                updateMenu(currentRestaurant.getId());
                JOptionPane.showMessageDialog(frame, "Menu updated!");
                updateRestaurantFields(restaurantPanel);
                break;
            case "add to menu":
                addToMenu();
                break;
            case "update selected order to next state":
                updateOrderToNextState();
                break;
            case "remove selected menu item":
                removeMenuItem();
                break;
            default:
                JOptionPane.showMessageDialog(frame, "A confused button click. What Do I do with " + e.getActionCommand() + "?");
                break;
        }
    }

    private void removeMenuItem() {
        if (!Objects.isNull(selectedMenuItem)) {
            currentMenu.getItems().removeIf(item -> item.getName().equals(selectedMenuItem));
        }
        populateMenuTableFromCurrentMenu();
    }

    /**
     * Perform a login
     */
    private void login() {
        final ResponseEntity<Void> loginResponse;
        try {
            loginResponse = authClient.login(new LoginRequest(login_usernameText.getText(), new String(login_passwordText.getPassword())));
        } catch (ClientException e) {
            showErrorDialogue(e);
            return;
        }
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
            //store token locally for future http calls
            token = loginResponse.getHeaders().get("Authorization").get(0);
            //get Restaurant
            try {
                currentRestaurant = restaurantClient.getRestaurantByOwner(token);
                if (!Objects.isNull(currentRestaurant)) {
                    currentMenu = menuClient.getMenu(currentRestaurant.getId());
                    updateOrderHistory();
                    initialiseOrderQueue();
                }
            } catch (ClientException e) {
                //do nothing. it's ok we we don't have a restaurant/menu yet
            }
            updateRestaurantFields(restaurantPanel);

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
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong Username and Password");
        }
    }

    /**
     * Create a Queue for receiving orders
     */
    private void initialiseOrderQueue() {
        if (Objects.isNull(queue)) {
            try {
                queue = session.createQueue("res-" + currentRestaurant.getName());
                final MessageConsumer consumer = session.createConsumer(queue);
                consumer.setMessageListener(new MessageListener());
            } catch (JMSException e) {
                e.printStackTrace();
            }
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
                    .withRole(UserRole.RESTAURANT)
                    .build());
        } catch (ClientException e) {
            showErrorDialogue(e);
            return;
        }
        if (newUser.getStatusCode() == HttpStatus.OK) {
            //clear fields
            register_emailText.setText("");
            register_passwordText.setText("");
            register_usernameText.setText("");

            //navigate to login
            tabbedPane.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(frame, "Oops, Something went wrong!");
        }
    }

    /**
     * Create a restaurant
     */
    private void createRestaurantAndMenu() {
        if (Objects.isNull(currentRestaurant)) {
            final ResponseEntity<Restaurant> restaurantResponse;
            try {
                restaurantResponse = restaurantClient.createRestaurant(token, CreateRestaurantParamsBuilder.aCreateRestaurantParams()
                        .withAddress(restaurant_addressText.getText())
                        .withName(restaurant_nameText.getText())
                        .build());
                if (restaurantResponse.getStatusCode() == HttpStatus.CREATED) {
                    final Restaurant restaurant = restaurantResponse.getBody();

                    currentRestaurant = restaurant;
                    updateMenu(restaurant.getId());
                    initialiseOrderQueue();
                    updateRestaurantFields(restaurantPanel);
                    JOptionPane.showMessageDialog(frame, "Restaurant created successfully!");
                }
            } catch (ClientException e) {
                showErrorDialogue(e);
            }

        }
    }

    private void updateMenu(final long restaurantId) {
        try {
            currentMenu = menuClient.createOrUpdateMenu(token, restaurantId, extractMenuParamsFromTable());
        } catch (ClientException e) {
            showErrorDialogue(e);
        }
    }

    private void showErrorDialogue(final ClientException e) {
        JOptionPane.showMessageDialog(frame, e.getExceptionResponse().getDescription());
    }

    private void updateOrderToNextState() {
        if (!Objects.isNull(selectedOrder)) {
            OrderState nextState;
            if (selectedOrder.getState().equals(OrderState.ACCEPTED)) {
                nextState = OrderState.PREPARING;
            } else if (selectedOrder.getState().equals(OrderState.PREPARING)) {
                nextState = OrderState.ON_THE_WAY;
            } else if (selectedOrder.getState().equals(OrderState.ON_THE_WAY)) {
                nextState = OrderState.DELIVERED;
            } else {
                //do nothing
                return;
            }
            try {
                orderClient.updateOrder(token, selectedOrder.getId(),
                        UpdateOrderParamsBuilder
                                .anUpdateOrderParams()
                                .withState(nextState)
                                .build());
            } catch (ClientException e) {
                e.printStackTrace();
            }
            updateOrderHistory();
        }
    }

    private CreateMenuParams extractMenuParamsFromTable() {
        Set<CreateItemParams> itemParams = new HashSet<>();
        final Object[][] tableData = new Object[menu_dtm.getRowCount()][menu_dtm.getColumnCount()];
        for (int i = 0; i < menu_dtm.getRowCount(); i++) {
            for (int j = 0; j < menu_dtm.getColumnCount(); j++) {
                tableData[i][j] = menu_dtm.getValueAt(i, j);
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
        menu_dtm.setRowCount(0);
        currentMenu.getItems()
                .forEach(item -> menu_dtm.addRow(new Object[]{item.getName(), item.getDescription(), item.getUnitPrice()}));
    }

    /**
     * Add item to menu table
     */
    private void addToMenu() {
        double price = Double.valueOf(unitPriceText.getValue().toString().replaceAll("[^\\d.]+", ""));
        menu_dtm.addRow(new Object[]{menu_nameText.getText(), menu_descriptionText.getText(), price});
        menu_nameText.setText("");
        menu_descriptionText.setText("");
        unitPriceText.setValue(0.0);
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
            order_dtm.addRow(new Object[]{order.getId(), order.getDeliveryAddress(), order.itemsAsString(), order.getTotalPrice(), expectedDeliveryTimeString, order.getState()});
        });
    }

    private void updateOrderHistory() {
        try {
            orderHistory = orderClient.getRestaurantOrders(token).stream()
                    .sorted(Comparator.comparingLong(RestaurantOrder::getId).reversed())
                    .collect(Collectors.toList());
        } catch (ClientException e) {
            showErrorDialogue(e);
        }
        if (orderHistory.isEmpty()) {
            updatedOrderButton.setEnabled(false);
        }
        populateOrderHistoryTableFromOrderHistory();
    }

    class MessageListener implements javax.jms.MessageListener {

        @Override
        public void onMessage(final Message message) {
            try {
                TextMessage textMessage = (TextMessage) message;
                final int select = JOptionPane.showConfirmDialog(frame, textMessage.getText(), "New Order", YES_NO_OPTION);
                try {

                    if (select == YES_OPTION) {
                        Object[] possibilities = {"15", "30", "45", "60", "75", "90"};
                        final String time = (String) JOptionPane.showInputDialog(frame, "I will deliver in (minutes):", "Delivery time", JOptionPane.PLAIN_MESSAGE, null, possibilities, "15");
                        final long expectedTimeOfDelivery = System.currentTimeMillis() + (Long.valueOf(time) * ONE_MINUTE_IN_MILLIS);
                        orderClient.updateOrder(token, textMessage.getLongProperty("id"), UpdateOrderParamsBuilder.anUpdateOrderParams()
                                .withExpectedDeliveryTime(expectedTimeOfDelivery)
                                .withState(OrderState.ACCEPTED)
                                .build());
                    } else if (select == NO_OPTION) {
                        orderClient.updateOrder(token, textMessage.getLongProperty("id"), UpdateOrderParamsBuilder.anUpdateOrderParams()
                                .withState(OrderState.DECLINED)
                                .build());
                    }
                } catch (ClientException e) {
                    showErrorDialogue(e);
                }
                updateOrderHistory();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
