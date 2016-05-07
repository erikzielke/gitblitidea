package dk.erikzielke.gitblit.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import dk.erikzielke.gitblit.GitblitApplicationComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GitblitSettingsConfigurable implements Configurable {
    private GitblitSettingsForm gitblitSettingsForm;

    @Nls
    @Override
    public String getDisplayName() {
        return "Gitblit Settings";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (gitblitSettingsForm == null) {
            gitblitSettingsForm = new GitblitSettingsForm();
        }
        return gitblitSettingsForm.getRoot();
    }

    @Override
    public boolean isModified() {
        GitblitSettings state = GitblitApplicationComponent.getInstance().getState();
        return gitblitSettingsForm.isModified(state);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (gitblitSettingsForm != null) {
            GitblitSettings state = GitblitApplicationComponent.getInstance().getState();
            gitblitSettingsForm.getData(state);
        }
    }

    @Override
    public void reset() {
        if (gitblitSettingsForm != null) {
            GitblitSettings state = GitblitApplicationComponent.getInstance().getState();
            gitblitSettingsForm.setData(state);
        }
    }

    @Override
    public void disposeUIResources() {

    }
}
