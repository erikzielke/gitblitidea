package dk.erikzielke.gitblit;

import dk.erikzielke.gitblit.api.GitBlitService;
import dk.erikzielke.gitblit.api.RepositoryModel;
import dk.erikzielke.gitblit.settings.GitblitSettings;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.log4j.Logger;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class GitblitUtil {
    private static final Logger LOG = Logger.getLogger(GitblitUtil.class);

    static List<RepositoryModel> getRepositories() {
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            GitblitSettings state = GitblitApplicationComponent.getInstance().getState();
            if (state != null) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(chain -> {
                            Request request = chain.request().newBuilder()
                                    .addHeader("Authorization", Credentials.basic(state.getUsername(),state.getPassword()))
                                    .build();

                            return chain.proceed(request);
                        })
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .baseUrl(state.getUrl() + "/rpc/")
                        .build();
                GitBlitService gitBlitService = retrofit.create(GitBlitService.class);
                Response<Map<String, RepositoryModel>> response = gitBlitService.getRepositories().execute();
                Map<String, RepositoryModel> map = response.body();
                for (Map.Entry<String, RepositoryModel> entry : map.entrySet()) {
                    String key = entry.getKey();
                    RepositoryModel model = entry.getValue();
                    model.setUrl(key);
                }
                return new ArrayList<>(map.values());
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            LOG.error(e, e);
        }
        return null;
    }
}
