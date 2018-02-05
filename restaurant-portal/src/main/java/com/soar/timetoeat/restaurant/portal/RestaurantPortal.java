package com.soar.timetoeat.restaurant.portal;

import com.soar.timetoeat.restaurant.portal.dao.AuthServiceClient;
import com.soar.timetoeat.restaurant.portal.domain.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
public class RestaurantPortal extends JFrame implements ActionListener {

    private static String token = null;

    private final AuthServiceClient authServiceClient;
    private JTextField userText;
    private JPasswordField passwordText;

    @Autowired
    public RestaurantPortal(final AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
        initWindow();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(RestaurantPortal.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            RestaurantPortal ex = ctx.getBean(RestaurantPortal.class);
            ex.setVisible(true);
        });
    }

    private void initWindow() {
        setSize(300, 180);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
    }

    private void placeComponents(JPanel panel) {

        panel.setLayout(null);

        JLabel userLabel = new JLabel("User");
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100, 10, 160, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 40, 160, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        JButton registerButton = new JButton("register");
        registerButton.setBounds(180, 80, 80, 25);
        panel.add(registerButton);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final ResponseEntity<Void> login = authServiceClient.login(new LoginRequest(userText.getText(), Arrays.toString(passwordText.getPassword())));
        System.out.println(login.getHeaders().get("Authorization"));
    }
}
