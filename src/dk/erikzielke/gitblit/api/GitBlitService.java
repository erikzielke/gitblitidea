package dk.erikzielke.gitblit.api;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.Map;

public interface GitBlitService {
    @GET("?req=LIST_REPOSITORIES")
    Call<Map<String,RepositoryModel>> getRepositories();
}
