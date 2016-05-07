package dk.erikzielke.gitblit.settings;

import javax.swing.*;
import java.util.Objects;

public class GitblitSettingsForm {
    private JTextField username;
    private JPasswordField password;
    private JPanel root;
    private JTextField url;

    public JTextField getUsername() {
        return username;
    }

    public JPasswordField getPassword() {
        return password;
    }

    public JPanel getRoot() {
        return root;
    }

    public void setData(GitblitSettings data) {
        username.setText(data.getUsername());
        password.setText(data.getPassword());
        url.setText(data.getUrl());
    }

    public void getData(GitblitSettings state) {
        state.setUrl(url.getText());
        state.setUsername(username.getText());
        state.setPassword(new String(password.getPassword()));
    }

    public boolean isModified(GitblitSettings state) {
        if (
                !Objects.equals(state.getUsername(), username.getText()) ||
                !Objects.equals(state.getPassword(), new String(password.getPassword())) ||
                !Objects.equals(state.getUrl(), url.getText())
            ) {
            return true;
        }
        return false;
    }
}
