package bdd;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Pedido;

public class AppRepositoryPedido implements OnPedidoResultCallback{
    private PedidoDao pedidoDao;
    private OnResultCallback callback;

    public AppRepositoryPedido (Application application, OnResultCallback context){
        AppDatabase db = AppDatabase.getInstance(application);
        pedidoDao = db.pedidoDao();
        callback = context;
    }

    public void insertar(final Pedido pedido){
        pedidoDao.insertar(pedido);
    }

    public void borrar(final Pedido pedido){
        pedidoDao.borrar(pedido);
    }

    public void actualizar(final Pedido pedido){
        pedidoDao.actualizar(pedido);
    }

    public void buscar(String id) {
        new BuscarPedidoById(pedidoDao, this).execute(id);
    }

    public void buscarTodos() {
        new BuscarPedidos(pedidoDao, this).execute();
    }

    @Override
    public void onResult(List<Pedido> pedidos) {
        Log.d("DEBUG", "Pedido found");
        callback.onResult(pedidos);
    }


    public interface OnResultCallback<T> {
        void onResult(List<T> result);
    }

    class BuscarPedidos extends AsyncTask<String, Void, List<Pedido>> {

        private PedidoDao dao;
        private OnPedidoResultCallback callback;

        public BuscarPedidos(PedidoDao dao, OnPedidoResultCallback context) {
            this.dao = dao;
            this.callback = context;
        }

        @Override
        protected List<Pedido> doInBackground(String... strings) {
            List<Pedido> pedidos = dao.buscarTodos();
            return pedidos;
        }

        @Override
        protected void onPostExecute(List<Pedido> pedidos) {
            super.onPostExecute(pedidos);
            callback.onResult(pedidos);
        }
    }

    class BuscarPedidoById extends AsyncTask<String, Void, List<Pedido>> {

        private PedidoDao dao;
        private OnPedidoResultCallback callback;

        public BuscarPedidoById(PedidoDao dao, OnPedidoResultCallback context) {
            this.dao = dao;
            this.callback = context;
        }

        @Override
        protected List<Pedido> doInBackground(String... strings) {
            List<Pedido> pedidos = new ArrayList<>();
            //pedidos.add(dao.buscar(strings[0]));
            return pedidos;
        }

        @Override
        protected void onPostExecute(List<Pedido> pedidos) {
            super.onPostExecute(pedidos);
            callback.onResult(pedidos);
        }
    }
}