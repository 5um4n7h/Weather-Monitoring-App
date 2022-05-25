package weather.monitoring






import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView


//import com.robinhood.spark.SparkAdapter
//import com.robinhood.spark.SparkView


class HumidityActivity : AppCompatActivity() {

    lateinit var tvCur : TextView
    lateinit var tvHis : TextView
    lateinit var tvHisHead: TextView
    lateinit var sparkView : SparkView
    val humList : MutableList<Float> = mutableListOf()
    val myRef = Firebase.database.reference
    var timeList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_humidity)

        sparkView = findViewById(R.id.sparkview)
        tvHis = findViewById(R.id.tvHistory)
        tvHisHead = findViewById(R.id.tvHisHead)
        // sparkView.adapter = MyAdapter(floatArrayOf())


//


        //sparkLineLayout.setData(humList as ArrayList<Int>)

//        humList.add(10F)
//        humList.add(20F)

        myRef.child("ESP8266").addValueEventListener(object : ValueEventListener {



            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "humList =  "+humList)

                var value = dataSnapshot.child("/Humidity").getValue().toString().toInt()
                Log.d(TAG, "onDataChange: "+value)
                humList.add(value.toFloat())
                if(humList.size>60){
                    humList.removeAt(0);
                }
                // Log.d(TAG, "onDataChange: "+humList)










                val time  = dataSnapshot.child("/time").getValue().toString()

                timeList.add(time)
                if(timeList.size>60){
                    timeList.removeAt(0);
                }

//                   UpdateGraph(value.toString().toFloat(),time)

                Log.d(TAG, "Value is: $value")
                Log.d(TAG, "Time is : $time")

                updateData();


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })





    }




    private fun updateData() {
        tvHis.text = ""
        tvHisHead.text = "Time\t\t\t\t\t\t\tHumidity in %\n"
        //sparkLineLayout.invalidate()
        Log.d(TAG, "humList size "+humList.size)
        Log.d(TAG, "humList :"+humList)

        var floatArray: FloatArray = humList.toTypedArray().toFloatArray()
        sparkView.adapter = MyAdapter(floatArray)



        if(humList.size==1){
            tvHis.append(
                timeList.get(0) +"\t\t\t\t\t\t\t\t\t" + humList.get(0).toString() +  "\n"
            )
        }

        else if(humList.size>1) {
            for (i in 0..humList.size-1){
                tvHis.append(timeList.get(i)+"\t\t\t\t\t\t\t\t\t"+ humList.get(i).toString()+"\n")
                if (humList.get(i)<40){
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage("Humidity is less than 40%! \n" +
                            "Can affect the people who have respiratory problems. ")
                        .setCancelable(true)
                        .setPositiveButton("Okay",
                            DialogInterface.OnClickListener { dialog, id -> this.finish() })

                        .setTitle("Alert!")
                        .setIcon(R.drawable.ic_baseline_warning_24)
                    val alert: AlertDialog = builder.create()
                    alert.show() //showing the dialog


                }

            }


        }


    }
    class MyAdapter(private val yData: FloatArray) : SparkAdapter() {
        override fun getCount(): Int {
            return yData.size
        }

        override fun getItem(index: Int): Any {
            return yData[index]
        }

        override fun getY(index: Int): Float {
            return yData[index]
        }
    }

//    private fun initBarGraph() {
//        var barDataSet = BarDataSet(addData(0F),"Dataset 1")
//
//
//        var barData = BarData()
//        barData.addDataSet(barDataSet)
//
//        barChart.data = barData
//        barChart.description = Description()
//        barChart.invalidate()
//
//        xAxisLabels.add("")
//        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
//    }

//    public fun addData(newVal : Float): ArrayList<BarEntry> {
//
//        if (arryList.size >= 5) {
//            arryList.removeAt(0)
//        }
//        if (arryList.size == 0) {
//
//            val itemVal = 0F
//            arryList.add(BarEntry(itemVal, newVal, "Data"))
//        } else {
//        val index = arryList.size - 1
//        val item: BarEntry = arryList.get(index)
//        val itemVal = item.x + 1F
//        arryList.add(BarEntry(itemVal, newVal, "Data"))
//    }
//        else{
//            val index = arryList.size - 1
//            val item: BarEntry = arryList.get(index)
//            val itemVal = item.x + 1F
//            arryList.add(BarEntry(itemVal, newVal, "Data"))
//        }
//        return arryList
//
//    }
//
//
//    private fun UpdateGraph(newVal: Float, time: String?) {
//
//
//        var barDataSet = BarDataSet(addData(newVal),"Dataset 1")
//
//
//        var barData = BarData()
//        barData.addDataSet(barDataSet)
//
//        barChart.data = barData
//        barChart.description = Description()
//        barChart.invalidate()
//
//
//        xAxisLabels.add(time!!)
//        xAxisLabels.removeAt(0)
//        Log.d(TAG, "UpdateGraph: "+xAxisLabels)
//        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
//
//
//
//    }


}