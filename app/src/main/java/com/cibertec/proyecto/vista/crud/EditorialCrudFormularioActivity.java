package com.cibertec.proyecto.vista.crud;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.entity.Editorial;
import com.cibertec.proyecto.service.ServiceEditorial;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.FunctionUtil;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorialCrudFormularioActivity extends NewAppCompatActivity {

    TextView txtTitulo;
    Button btnRegistra;
    EditText txtNombre, txtDireccion, txtFechaCreacion;
    Spinner spnPais;

    //COnexion
    ServiceEditorial api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_crud_formulario);

        txtTitulo = findViewById(R.id.txtCrudEdiTitulo);
        btnRegistra = findViewById(R.id.btnCrudRegistraEdiEnviar);

        txtNombre = findViewById(R.id.txtCrudRegistraEdiNombre);
        txtDireccion = findViewById(R.id.txtCrudRegistraEdiDirecccion);
        txtFechaCreacion = findViewById(R.id.txtCrudRegistraEdiFechaCreacion);
        spnPais = findViewById(R.id.spnCrudRegistraEdiPais);

        Bundle extras = getIntent().getExtras();
        String metodo = extras.getString("var_tipo");

        api = ConnectionRest.getConnection().create(ServiceEditorial.class);

        if (metodo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Editorial - Registra");
            btnRegistra.setText("Registra");
        }else if (metodo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Editorial - Actualiza");
            btnRegistra.setText("Actualiza");

            Editorial objEditorial  =  (Editorial)extras.get("var_item");
            txtNombre.setText(objEditorial.getNombre());
            txtDireccion.setText(objEditorial.getDireccion());
            txtFechaCreacion.setText(objEditorial.getFechaCreacion());

            int posSeleccionado = FunctionUtil.getIndex(spnPais, objEditorial.getPais());
            spnPais.setSelection(posSeleccionado);

        }


    }


    public void inserta(Editorial obj){
        Call<Editorial> call = api.registraEditorial(obj);
        call.enqueue(new Callback<Editorial>() {
            @Override
            public void onResponse(Call<Editorial> call, Response<Editorial> response) {
                if (response.isSuccessful()){
                    Editorial objSalida = response.body();
                    if (objSalida == null){
                        mensajeAlert("ERROR -> NO se insertó");
                    }else{
                        mensajeAlert("ÉXITO -> Se insertó correctamente : " + objSalida.getIdEditorial());
                    }
                }else{
                    mensajeAlert("ERROR -> Error en la respuesta");
                }
            }
            @Override
            public void onFailure(Call<Editorial> call, Throwable t) {
                mensajeAlert("ERROR -> " +   t.getMessage());
            }
        });
    }

    public void actualiza(Editorial obj){
        Call<Editorial> call = api.actualizaEditorial(obj);
        call.enqueue(new Callback<Editorial>() {
            @Override
            public void onResponse(Call<Editorial> call, Response<Editorial> response) {
                if (response.isSuccessful()){
                    Editorial objSalida = response.body();
                    if (objSalida == null){
                        mensajeToastLong("ERROR -> NO se actualizó");
                    }else{
                        mensajeToastLong("ÉXITO -> Se actualizó correctamente : " + objSalida.getIdEditorial());
                    }
                }else{
                    mensajeAlert("ERROR -> Error en la respuesta");
                }
            }
            @Override
            public void onFailure(Call<Editorial> call, Throwable t) {
                mensajeAlert("ERROR -> " +   t.getMessage());
            }
        });
    }

    public void elimina(int id) {
        Call<Editorial> call = api.eliminaEditorial(id);
        call.enqueue(new Callback<Editorial>() {
            @Override
            public void onResponse(Call<Editorial> call, Response<Editorial> response) {
                if (response.isSuccessful()) {
                    Editorial objSalida = response.body();
                    if (objSalida == null){
                        mensajeAlert("ERROR -> NO se eliminó");
                    }else{
                        mensajeAlert("ÉXITO -> Se eliminó correctamente : " + objSalida.getIdEditorial());
                    }
                }else{
                    mensajeAlert("ERROR -> Error en la respuesta");
                }
            }
            @Override
            public void onFailure(Call<Editorial> call, Throwable t) {
                mensajeAlert("ERROR -> " +   t.getMessage());
            }
        });
    }


}