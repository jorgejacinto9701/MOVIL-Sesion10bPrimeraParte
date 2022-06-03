package com.cibertec.proyecto.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cibertec.proyecto.R;
import com.cibertec.proyecto.adapter.EditorialAdapter;
import com.cibertec.proyecto.entity.Editorial;
import com.cibertec.proyecto.service.ServiceEditorial;
import com.cibertec.proyecto.util.ConnectionRest;
import com.cibertec.proyecto.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditorialCrudListaActivity extends NewAppCompatActivity {

    EditText txtNombre;
    Button btnFiltrar, btnRegistra;


    //Listview y Adaptador
    ListView lstEditorial;
    List<Editorial> data = new ArrayList<Editorial>();
    EditorialAdapter adaptador;

    //COnexion
    ServiceEditorial api;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_crud_lista);

        txtNombre = findViewById(R.id.txtCrudEdiNombre);
        btnFiltrar = findViewById(R.id.btnCrudEdiFiltrar);
        btnRegistra = findViewById(R.id.btnCrudEdiRegistrar);

        //construir el listview y adaptador
        lstEditorial = findViewById(R.id.lstCrudEditorial);
        adaptador = new EditorialAdapter(this,R.layout.activity_editorial_crud_item, data);
        lstEditorial.setAdapter(adaptador);

        api = ConnectionRest.getConnection().create(ServiceEditorial.class);

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtNombre.getText().toString();
                consulta(filtro);
            }
        });

        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Levantar el Activity EditorialCrudForularioActivity  en MODO Registra
                Intent intent = new Intent(EditorialCrudListaActivity.this,EditorialCrudFormularioActivity.class);
                intent.putExtra("var_tipo","REGISTRAR");
                startActivity(intent);
            }
        });

        lstEditorial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Se obtiene la editorial seleccionada
                Editorial objEditorial = data.get(i);

                //Levantar el Activity EditorialCrudForularioActivity  en MODO Actualiza
                Intent intent = new Intent(EditorialCrudListaActivity.this,EditorialCrudFormularioActivity.class);
                intent.putExtra("var_tipo","ACTUALIZAR");
                intent.putExtra("var_item", objEditorial);
                startActivity(intent);
            }
        });

    }


    public void consulta(String filtro){
        Call<List<Editorial>> call = api.listaEditoriPorNombre(filtro);
        call.enqueue(new Callback<List<Editorial>>() {
            @Override
            public void onResponse(Call<List<Editorial>> call, Response<List<Editorial>> response) {
                if (response.isSuccessful()){
                    List<Editorial> lstEditorial = response.body();
                    mensajeAlert("Llegaron " + lstEditorial.size() + " elementos ") ;
                    data.clear();
                    data.addAll(lstEditorial);
                    adaptador.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Editorial>> call, Throwable t) {
                mensajeAlert("ERROR -> Error en la respuesta" + t.getMessage());
            }
        });
    }







}