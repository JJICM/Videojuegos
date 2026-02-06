package it.etg.pmpm.game.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import it.etg.pmpm.game.R
import it.etg.pmpm.game.data.local.GamesDao
import it.etg.pmpm.game.data.local.GamesDatabase
import it.etg.pmpm.game.data.local.GamesEntity
import it.etg.pmpm.game.data.remote.GamesAPIService
import it.etg.pmpm.game.databinding.ActivityGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameActivity : AppCompatActivity() {

    companion object {
        const val BASE_URL = "https://www.freetogame.com/api/"
        lateinit var database: GamesDatabase
        const val DB_NAME = "games-db"
    }

    lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createDB()
        binding.btnBuscar.setOnClickListener {
            val id = binding.etBuscar.text.toString().toIntOrNull()
            show(createAdapter(binding.listView), id)
        }

    }

    private fun createDB() {
        database = Room.databaseBuilder(this, GamesDatabase::class.java, DB_NAME).build()
    }

    private fun createAdapter(listView: ListView): ArrayAdapter<String> {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList<String>())
        listView.adapter = adapter
        return adapter
    }

    private fun show(adapter: ArrayAdapter<String>, id: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            val gamesDao = database.gamesDao()
            var games = gamesDao.getAll()
            if (games.isEmpty()) {
                getApi(gamesDao)
                games = gamesDao.getAll()
            }
            val datos = ArrayList<String>()
            if (id != null) {
                val game = gamesDao.getById(id)
                datos.add(getString(R.string.msg_games_info, game?.title, game?.genre, game?.platform))
            } else {
                games.forEach { game ->
                    datos.add(getString(R.string.msg_games_info, game.title, game.genre, game.platform))
                }
            }

            withContext(Dispatchers.Main) {
                adapter.clear()
                adapter.addAll(datos)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private suspend fun getApi(gamesDao: GamesDao) {
        val call = getRetrofit().create(GamesAPIService::class.java).getGames()
        if (call.isSuccessful) {
            val games = call.body()
            games?.forEach { game ->
                gamesDao.insert(GamesEntity(game.id, game.title, game.genre, game.platform))
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@GameActivity,
                    getString(R.string.msg_error_api),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}