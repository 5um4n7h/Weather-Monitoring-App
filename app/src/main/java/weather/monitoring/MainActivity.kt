package weather.monitoring

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnHum = findViewById<Button>(R.id.btnHum)

        btnHum.setOnClickListener{

            var intent : Intent = Intent(this,HumidityActivity::class.java)
            startActivity(intent)

        }

    }
}