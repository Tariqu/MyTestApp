package com.obaap.mytestapp

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.View
import android.widget.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var actv:AutoCompleteTextView?=null
    var lview:ListView?=null
    var list:ArrayList<String>?=null
    var l:Array<String>?=null
    var path:String?=null
    var temp:String?=null
    var stack:Stack<String>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stack= Stack()
        var status = checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if(status==PackageManager.PERMISSION_GRANTED){
            FileRead()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }

    }
    fun FileRead(){
        actv=findViewById(R.id.actv)
        list =ArrayList<String>()
        list!!.add("India")
        list!!.add("Ireland")
        list!!.add("Iran")
        list!!.add("Indonasia")
        list!!.add("Italy")
        list!!.add("IceLand")
        var ary=ArrayAdapter<String>(this,R.layout.dropdown,list)
        actv!!.setAdapter(ary)
        actv!!.threshold=1
        lview=findViewById(R.id.lview)
        path="/storage/emulated/0/"
        NextFile()
        lview!!.onItemClickListener = object :AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val s = lview!!.getItemAtPosition(p2).toString()
                //Toast.makeText(this@MainActivity,l?.get(p2),Toast.LENGTH_SHORT).show()
                temp =path
                path =path + l!!.get(p2)+"/"
                NextFile()

            }

        }
    }
    fun NextFile(){
        var f=File(path)
        if(!f.exists()){
            path="/storage/sdcard0/"
            f=File(path)
        }
        if(f.isDirectory) {
            l = f.list()
            var myadapter = ArrayAdapter<String>(this@MainActivity, R.layout.dropdown, l)
            lview!!.adapter = myadapter
            stack!!.push(path)
        }else{
            path=temp
            f=File(path)
            l = f.list()
            var myadapter = ArrayAdapter<String>(this@MainActivity, R.layout.dropdown, l)
            lview!!.adapter = myadapter
        }
    }
    fun Back(v: View){
        if(stack!!.size > 1){
            stack!!.pop()
            path=stack!!.peek()
            var f=File(path)
            l = f.list()
            var myadapter = ArrayAdapter<String>(this@MainActivity, R.layout.dropdown, l)
            lview!!.adapter = myadapter
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
            FileRead()
        }else{
            Toast.makeText(this@MainActivity,"Permission Required to run this App",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
