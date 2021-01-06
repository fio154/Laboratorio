package retrofit;
import java.util.List;

import model.Pedido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PedidoService {
    @GET("pedido/{id}")
    Call<Pedido> getPedido(@Path("id") String id);

    @GET("pedido/list")
    Call<List<Pedido>> getPedidoList();

    @POST("pedido")
    Call<Pedido> createPedido(@Body Pedido pedido);

}