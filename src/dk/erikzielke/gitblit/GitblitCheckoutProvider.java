package dk.erikzielke.gitblit;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckoutProvider;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ThrowableConvertor;
import com.intellij.util.containers.Convertor;
import dk.erikzielke.gitblit.api.RepositoryModel;
import git4idea.actions.BasicAction;
import git4idea.checkout.GitCheckoutProvider;
import git4idea.checkout.GitCloneDialog;
import git4idea.commands.Git;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GitblitCheckoutProvider implements CheckoutProvider {
    @Override
    public void doCheckout(@NotNull Project project, @Nullable Listener listener) {
        BasicAction.saveAll();




        List<RepositoryModel> availableRepos;
        availableRepos = computeValueInModal(project, "Access to GitBlit", false, progressIndicator -> {
            List<RepositoryModel> repositoryModels = null;
            try {
                repositoryModels = runTask(
                        aVoid -> GitblitUtil.getRepositories()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            return repositoryModels;
        });

        final GitCloneDialog dialog = new GitCloneDialog(project);
        // Add predefined repositories to history
        dialog.prependToHistory("-----------------------------------------------");
        for (int i = availableRepos.size() - 1; i >= 0; i--) {
            dialog.prependToHistory(availableRepos.get(i).getUrl());
        }
        if (!dialog.showAndGet()) {
            return;
        }
        dialog.rememberSettings();
        final VirtualFile destinationParent = LocalFileSystem.getInstance().findFileByIoFile(new File(dialog.getParentDirectory()));
        if (destinationParent == null) {
            return;
        }
        final String sourceRepositoryURL = dialog.getSourceRepositoryURL();
        final String directoryName = dialog.getDirectoryName();
        final String parentDirectory = dialog.getParentDirectory();

        Git git = ServiceManager.getService(Git.class);
        GitCheckoutProvider.clone(project, git, listener, destinationParent, sourceRepositoryURL, directoryName, parentDirectory);
    }

    private static <T> T computeValueInModal(@NotNull Project project,
                                             @NotNull String caption,
                                             boolean canBeCancelled,
                                             @NotNull final Convertor<ProgressIndicator, T> task) {
        return ProgressManager.getInstance().run(new Task.WithResult<T, RuntimeException>(project, caption, canBeCancelled) {
            @Override
            protected T compute(@NotNull ProgressIndicator indicator) {
                return task.convert(indicator);
            }
        });
    }

    private static <T> T runTask(@NotNull ThrowableConvertor<Void, T, IOException> task) throws IOException {
        return task.convert(null);
    }

    @Override
    public String getVcsName() {
        return "GitBlit";
    }
}
