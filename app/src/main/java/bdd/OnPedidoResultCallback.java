package bdd;

import java.util.List;

import model.Pedido;

public interface OnPedidoResultCallback {
    void onResult(List<Pedido> pedido);
}
