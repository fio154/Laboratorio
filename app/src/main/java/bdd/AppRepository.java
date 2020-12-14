package bdd;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Pedido;
import model.Plato;

public class AppRepository implements OnPlatoResultCallback{
    private PlatoDao platoDao;
    private OnResultCallback callback;

    public AppRepository(Application application, OnResultCallback context){
        AppDatabase db = AppDatabase.getInstance(application);
        platoDao = db.platoDao();
        callback = context;
    }

    public void insertar(final Plato plato){
        platoDao.insertar(plato);
    }

    public void borrar(final Plato plato){
        platoDao.borrar(plato);
    }

    public void actualizar(final Plato plato){
        platoDao.actualizar(plato);
    }

    public void buscar(String id) {
        new BuscarPlatoById(platoDao, this).execute(id);
    }

    public void buscarTodos() {
        new BuscarPlatos(platoDao, this).execute();
    }

    @Override
    public void onResult(List<Plato> platos) {
        Log.d("DEBUG", "Plato found");
        callback.onResult(platos);
    }

    public interface OnResultCallback<T> {
        void onResult(List<T> result);
    }

    class BuscarPlatos extends AsyncTask<String, Void, List<Plato>> {

        private PlatoDao dao;
        private OnPlatoResultCallback callback;

        public BuscarPlatos(PlatoDao dao, OnPlatoResultCallback context) {
            this.dao = dao;
            this.callback = context;
        }

        @Override
        protected List<Plato> doInBackground(String... strings) {
            List<Plato> platos = dao.buscarTodos();
            return platos;
        }

        @Override
        protected void onPostExecute(List<Plato> platos) {
            super.onPostExecute(platos);
            callback.onResult(platos);
        }
    }

    class BuscarPlatoById extends AsyncTask<String, Void, List<Plato>> {

        private PlatoDao dao;
        private OnPlatoResultCallback callback;

        public BuscarPlatoById(PlatoDao dao, OnPlatoResultCallback context) {
            this.dao = dao;
            this.callback = context;
        }

        @Override
        protected List<Plato> doInBackground(String... strings) {
            List<Plato> platos = new ArrayList<>();
            platos.add(dao.buscar(strings[0]));
            return platos;
        }

        @Override
        protected void onPostExecute(List<Plato> platos) {
            super.onPostExecute(platos);
            callback.onResult(platos);
        }
    }
}