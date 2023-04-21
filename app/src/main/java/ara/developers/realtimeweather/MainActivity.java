package ara.developers.realtimeweather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView location, currentTemp, weatherCondition, forecast_1_day, forecast_1_minmax, forecast_2_day, forecast_2_minmax, forecast_3_day, forecast_3_minmax;
    private String locationName, url;
    private final String api = "127714a8e5cf4b4cba4113438231003";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = findViewById(R.id.location);
        currentTemp = findViewById(R.id.current_temp);
        weatherCondition = findViewById(R.id.current_weather_condition);
        forecast_1_day = findViewById(R.id.forecast_1_day);
        forecast_1_minmax = findViewById(R.id.forecast_1_minmax);
        forecast_2_day = findViewById(R.id.forecast_2_day);
        forecast_2_minmax = findViewById(R.id.forecast_2_minmax);
        forecast_3_day = findViewById(R.id.forecast_3_day);
        forecast_3_minmax = findViewById(R.id.forecast_3_minmax);

        location.setOnClickListener(this);

        // Crea un objeto RequestQueue
        queue = Volley.newRequestQueue(this);

        // Default location
        locationName = "Tarragona";

        getJson();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location:
                dialog();
                break;
        }
    }

    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Datos");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_items, null);
        builder.setView(view);

        final EditText etNewLocation = view.findViewById(R.id.newLocation);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(TextUtils.isEmpty(etNewLocation.getText().toString().trim())){
                    Toast.makeText(MainActivity.this, "Insert valid location", Toast.LENGTH_SHORT).show();
                }else{
                    locationName = etNewLocation.getText().toString().trim();
                    getJson();
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getJson(){

        // URL de la API
        url = "https://api.weatherapi.com/v1/current.json?key=" + api + "&q=" + locationName + "&days=3&aqi=no&alerts=no";

        // Crea una solicitud JsonObjectRequest para la API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesa la respuesta JSON
                        try {
                            // LOCATION OBJECT
                            JSONObject locationObj = response.getJSONObject("location");
                            // LOCATION OBJECT - NAME
                            String name = locationObj.getString("name");
                            // CURRENT OBJECT
                            JSONObject currentObj = response.getJSONObject("current");
                            // CURRENT OBJECT - TEMP C
                            String cTemp = currentObj.getString("temp_c");
                            // CURRENT OBJECT - CONDITION OBJECT
                            JSONObject conditionObj = currentObj.getJSONObject("condition");
                            // CURRENT OBJECT - CONDITION OBJECT - TEXT
                            String conditionText = conditionObj.getString("text");
                            // FORECAST OBJECT
                            /*JSONObject forecastObj = response.getJSONObject("forecast");
                            JSONObject forecastdayArr = forecastObj.getJSONObject("forecastday");

                            // Itera sobre los elementos del array y selecciona los días deseados
                            for (int i = 0; i < forecastdayArr.length(); i++) {
                                JSONObject forecastdayObj = forecastdayArr.getJSONObject(i); // ???????????????????????????????????????????
                                String forecastDay = forecastdayObj.getString("date");
                                if (forecastDay.equals("2023-03-17") || forecastDay.equals("2023-03-18") || forecastDay.equals("2023-03-19")) {
                                    JSONObject dayObj = forecastdayObj.getJSONObject("day");
                                    String maxtemp_c = dayObj.getString("maxtemp_c");
                                    String mintemp_c = dayObj.getString("mintemp_c");
                                    // Utiliza los datos como sea necesario
                                    switch (forecastDay){
                                        case "2023-03-17":
                                            forecast_1_day.setText(forecastDay);
                                            forecast_1_minmax.setText(maxtemp_c + "/" + mintemp_c);
                                            break;
                                        case "2023-03-18":
                                            forecast_2_day.setText(forecastDay);
                                            forecast_2_minmax.setText(maxtemp_c + "/" + mintemp_c);
                                            break;
                                        case "2023-03-19":
                                            forecast_3_day.setText(forecastDay);
                                            forecast_3_minmax.setText(maxtemp_c + "/" + mintemp_c);
                                            break;
                                    }
                                }
                            }*/

                            // Muestra el valor en el TextView
                            location.setText(name);
                            currentTemp.setText(cTemp);
                            weatherCondition.setText(conditionText);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja los errores de la solicitud
                        Toast.makeText(MainActivity.this,"Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Agrega la solicitud a la cola de solicitudes
        queue.add(jsonObjectRequest);
    }

}