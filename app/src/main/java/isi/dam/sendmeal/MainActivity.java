package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import model.CuentaBancaria;
import model.Tarjeta;
import model.Usuario;

public class MainActivity extends AppCompatActivity {

    EditText nombre, contrasena, email, repetir_contrasena, num_tarjeta, ccv, cbu, alias_cbu, mes, anio;
    TextView credito_inicial, tipo_tarjeta, mensaje_exito;
    SeekBar skb;
    Switch carga;
    CheckBox acepto_terminos;
    Button registrar;
    RadioButton credito, debito;
    RadioGroup radio;
    int ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText) findViewById(R.id.nombre);
        contrasena = (EditText) findViewById(R.id.contraseña);
        email = (EditText) findViewById(R.id.email);
        repetir_contrasena = (EditText) findViewById(R.id.repetir_contraseña);
        num_tarjeta = (EditText) findViewById(R.id.numero_tarjeta);
        ccv = (EditText) findViewById(R.id.ccv);
        cbu = (EditText) findViewById(R.id.cbu);
        alias_cbu = (EditText) findViewById(R.id.alias_cbu);
        mes = (EditText) findViewById(R.id.mes);
        anio = (EditText) findViewById(R.id.año);
        credito_inicial = (TextView) findViewById(R.id.credito_inicial);
        skb = (SeekBar) findViewById(R.id.seekBar);
        carga = (Switch) findViewById(R.id.carga);
        acepto_terminos = (CheckBox) findViewById(R.id.terminos);
        registrar = (Button) findViewById(R.id.registrar);
        credito = (RadioButton) findViewById(R.id.credito);
        debito = (RadioButton) findViewById(R.id.debito);
        tipo_tarjeta = (TextView) findViewById(R.id.tipo_tarjeta);
        radio = (RadioGroup) findViewById(R.id.radioGroup);
        mensaje_exito = (TextView) findViewById(R.id.mensaje_exito);

        registrar.setEnabled(false);
        anio.setEnabled(false);
        ccv.setEnabled(false);
        mensaje_exito.setVisibility(View.GONE);
        credito_inicial.setVisibility(View.GONE);
        skb.setVisibility(View.GONE);


        acepto_terminos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(acepto_terminos.isChecked()){
                    registrar.setEnabled(true);
                }else{
                    registrar.setEnabled(false);
                    mensaje_exito.setVisibility(View.GONE);
                }
            }
        });

       registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()){
                    mensaje_exito.setVisibility(View.VISIBLE);
                    String nombre_str = nombre.getText().toString();
                    String contrasena_str = contrasena.getText().toString();
                    String repetir_str = repetir_contrasena.getText().toString();
                    String email_str = email.getText().toString();
                    String num_tarjeta_str = num_tarjeta.getText().toString();
                    String ccv_str = ccv.getText().toString();
                    String cbu_str = cbu.getText().toString();
                    String alias_str = alias_cbu.getText().toString();
                    String mes_str = mes.getText().toString();
                    String anio_str = anio.getText().toString();
                    int skb_int = skb.getProgress();
                    boolean credito_boolean = credito.isChecked();
                    Calendar fecha_vencimiento = Calendar.getInstance();
                    fecha_vencimiento.set(Integer.parseInt(anio_str), Integer.parseInt(mes_str), 1);
                    ID++;

                    CuentaBancaria cuenta = new CuentaBancaria(cbu_str, alias_str);
                    Tarjeta tarjeta = new Tarjeta(num_tarjeta_str,ccv_str,fecha_vencimiento,credito_boolean);
                    Usuario usuario = new Usuario(ID, nombre_str,contrasena_str,email_str, skb_int, tarjeta, cuenta);
                }else {
                    mensaje_exito.setVisibility(View.GONE);
                }
            }
        });
        mes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    anio.setEnabled(false);
                }else{
                    anio.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    anio.setEnabled(false);
                }else{
                    anio.setEnabled(true);
                }
            }
        });

        num_tarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    ccv.setEnabled(false);
                }else{
                    ccv.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()){
                    ccv.setEnabled(false);
                }else{
                    ccv.setEnabled(true);
                }
            }
        });

        carga.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(carga.isChecked()){
                    credito_inicial.setVisibility(View.VISIBLE);
                    skb.setVisibility(View.VISIBLE);

                    credito_inicial.setText("Crédito inicial: " + skb.getProgress() + "/" + skb.getMax());

                    skb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                            credito_inicial.setText("Crédito inicial: " + progress + "/" + skb.getMax());
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                }else{
                    credito_inicial.setVisibility(View.GONE);
                    skb.setVisibility(View.GONE);
                }
            }
        });


    }

    public boolean validar(){
        boolean retorno = true;
        String email_str = email.getText().toString();
        String contrasena_str = contrasena.getText().toString();
        String repetir_str = repetir_contrasena.getText().toString();
        String num_str = num_tarjeta.getText().toString();
        String ccv_str = ccv.getText().toString();
        String mes_str = mes.getText().toString();
        String anio_str = anio.getText().toString();
        String expresion = "[^@]*(@[A-Za-z]{3}[^@]*)+";

        if(email_str.isEmpty()){
            email.setError("E-mail es un campo obligatorio");
            retorno = false;
        }else if(!Pattern.matches(expresion,email_str)){
            email.setError("Debe contener al menos un @ y 3 letras después de este");
            retorno = false;
        }
        if(contrasena_str.isEmpty()){
            contrasena.setError("Contraseña es un campo obligatorio");
            retorno = false;
        }
        if(num_str.isEmpty()){
            num_tarjeta.setError("Número de tarjeta es un campo obligatorio");
            retorno = false;
        }
        if(ccv_str.isEmpty()){
            ccv.setError("CCV es un campo obligatorio");
            retorno = false;
        }
        if(mes_str.isEmpty()){
            mes.setError("Mes es un campo obligatorio");
            retorno = false;
        }else if(Integer.parseInt(mes_str)>12 || Integer.parseInt(mes_str)<1){
            mes.setError("Mes no válido");
            retorno = false;
        }
        if(anio_str.isEmpty()){
            anio.setError("Año es un campo obligatorio");
            retorno = false;
        }else if(calcularMeses(Integer.parseInt(mes_str),Integer.parseInt(anio_str))<4){
            anio.setError("La fecha tiene que ser superior a 3 meses de la actual");
            retorno = false;
        }
        if(!repetir_str.equals(contrasena_str)){
            repetir_contrasena.setError("Las contraseñas no coinciden");
            retorno = false;
        }
        if(!credito.isChecked() && !debito.isChecked()){
            Toast.makeText(this, "Debe seleccionar un tipo de tarjeta", Toast.LENGTH_SHORT).show();
            retorno = false;
        }
        if(carga.isChecked() && skb.getProgress()==0) {
            Toast.makeText(this, "Crédito inicial debe ser mayor a $0", Toast.LENGTH_SHORT).show();
            retorno = false;
        }
        return retorno;
    }

    public static int calcularMeses(int mes, int anio2){
        Calendar fecha_vencimiento = Calendar.getInstance();
        fecha_vencimiento.set(anio2,mes-1, 1);

        Calendar actual = Calendar.getInstance();

        int diferencia_anios = calcularAnios(mes,anio2);

        int diferencia_meses = (diferencia_anios*12) + (fecha_vencimiento.get(Calendar.MONTH) - actual.get(Calendar.MONTH));

        return diferencia_meses;
    }

    public static int calcularAnios(int mes, int anio2){
        Calendar fecha_vencimiento = Calendar.getInstance();
        fecha_vencimiento.set(anio2,mes-1, 1);

        Calendar actual = Calendar.getInstance();

        int diferencia = fecha_vencimiento.get(Calendar.YEAR) - actual.get(Calendar.YEAR);

        return diferencia;
    }
}