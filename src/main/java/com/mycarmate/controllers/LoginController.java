import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errorText;

    @FXML
    private ImageView logoImageView;

    @FXML
    public void initialize() {
        // Set logo image
        Image logoImage = new Image("file:src/main/resources/assets/mycarmate-high-resolution-logo-transparent.png");
        logoImageView.setImage(logoImage);
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Email and password are required.");
            return;
        }

        try {
            // Mock Firebase authentication call
            boolean loginSuccess = authenticateWithFirebase(email, password);

            if (loginSuccess) {
                // Make a backend API call to get the username
                String username = fetchUsernameFromBackend();
                System.out.println("Login successful! Username: " + username);

                // Navigate to dashboard
                navigateToDashboard(username);
            } else {
                showError("Login failed. Please check your credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred. Please try again later.");
        }
    }

    @FXML
    private void handleSignUp() {
        // Handle navigation to Sign Up page
        System.out.println("Navigating to Sign Up page...");
        // Implement navigation logic here
    }

    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisible(true);
    }

    private boolean authenticateWithFirebase(String email, String password) {
        // TODO: Integrate Firebase authentication here
        System.out.println("Authenticating with Firebase: " + email);
        return true; // Mock authentication success
    }

    private String fetchUsernameFromBackend() throws Exception {
        // Mock backend call
        URL url = new URL("http://localhost:5050/getUsername");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String requestBody = "{\"uid\": \"mockUID\"}";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes());
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            System.out.println("Backend response: " + response);
            return "MockUsername"; // Extract username from response (mocked here)
        }
    }

    private void navigateToDashboard(String username) {
        System.out.println("Navigating to dashboard with username: " + username);
        // Implement navigation logic here
    }
}
