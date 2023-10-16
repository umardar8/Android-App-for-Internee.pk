package pk.internee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "internee.pk database").build()
        val internshipDao = db.internshipDao()
        val internship = internshipDao.getAll()

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment(), it.title.toString())
                R.id.about -> replaceFragment(AboutFragment(), it.title.toString())
                R.id.contact -> replaceFragment(ContactFragment(), it.title.toString())
                R.id.lms -> replaceFragment(LMSFragment(), it.title.toString())
                R.id.interneePortal -> replaceFragment(InterneeFragment(), it.title.toString())
                R.id.internships -> replaceFragment(InternshipsFragment(), it.title.toString())
                R.id.jobPortal -> replaceFragment(JobFragment(), it.title.toString())
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
        setTitle(title)
        drawerLayout.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    // implementing Room database and data persistence in internee.pk app
    @Entity
    data class Internship(
        @PrimaryKey val id: Int,
        val name: String,
        val duration: Int
    )

    @Dao
    interface InternshipDao {
        @Query("SELECT * FROM Internship")
        fun getAll(): List<Internship>

        @Insert
        fun insertAll(vararg internship: Internship)

        @Delete
        fun delete(internship: Internship)
    }

    @Database(entities = [Internship::class], version = 1)
    abstract class AppDatabase: RoomDatabase() {
        abstract fun internshipDao(): InternshipDao
    }

}