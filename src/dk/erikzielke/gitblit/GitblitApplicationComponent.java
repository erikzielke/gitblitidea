package dk.erikzielke.gitblit;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import dk.erikzielke.gitblit.settings.GitblitSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "GitBlit", storages = {@Storage(id = "GitBlit", file = "$APP_CONFIG$/GitBlit.xml")})
public class GitblitApplicationComponent implements ApplicationComponent, PersistentStateComponent<GitblitSettings> {
    private GitblitSettings state;

    @Override
    public void initComponent() {
        if (state == null) {
            state = new GitblitSettings();
        }
    }

    @Override
    public void disposeComponent() {

    }

    public static GitblitApplicationComponent getInstance() {
        return ApplicationManager.getApplication().getComponent(GitblitApplicationComponent.class);
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Gitblit ";
    }

    @Nullable
    @Override
    public GitblitSettings getState() {
        return state;
    }

    @Override
    public void loadState(GitblitSettings state) {
        this.state = state;
    }
}
