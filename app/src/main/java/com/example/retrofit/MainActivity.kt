package com.example.retrofit

import android.net.http.HttpException
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit.databinding.ActivityMainBinding
import com.example.retrofit.databinding.ItemTodoBinding
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.http.Tag
const val TAG="MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecylcerView()
        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible=true
            val response=try{
                RetrofitInstance.api.getTodos()
            }catch (e:IOException){
                Log.e(TAG, "IOEXCEPTION", )

                binding.progressBar.isVisible=false
                return@launchWhenCreated

            }
            catch (e: HttpException){
                Log.e(TAG, "HTTPEXCEPTION", )

                binding.progressBar.isVisible=false
                return@launchWhenCreated

            }
            if(response.isSuccessful && response.body()!=null){
                todoAdapter.todos=response.body()!!
            }else{
                Log.e(TAG, "Response undefined", )
            }
            binding.progressBar.isVisible=false
        }

    }


    private fun setupRecylcerView() = binding.rvTodos.apply {
        todoAdapter=TodoAdapter()
        adapter=todoAdapter
        layoutManager=LinearLayoutManager(this@MainActivity)
    }
}