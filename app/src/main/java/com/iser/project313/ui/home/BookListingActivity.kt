package com.iser.project313.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.iser.project313.R
import com.iser.project313.ui.user_info.CheckSessionActivity
import kotlinx.android.synthetic.main.activity_book_listing.*
import kotlinx.android.synthetic.main.book_listing_content.*
import kotlinx.android.synthetic.main.book_listing_main_content.*

class BookListingActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_listing)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawer_layout, R.string.app_name, R.string.app_name)
        actionBarDrawerToggle.syncState()
        drawer_layout.setDrawerListener(actionBarDrawerToggle)
        title = "Books"
        rv_book_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_book_list.adapter = BookListingAdapter(getDataSet(),
            AdapterView.OnItemClickListener { _, p1, p2, _ ->
                val intent = Intent(this, BookDetailActivity::class.java)
                intent.putExtra("bookDetail", getDataSet()[p2])
                startActivity(intent)
            })
        val view = nav_view.getHeaderView(nav_view.headerCount - 1)
        view.findViewById<TextView>(R.id.tv_username).text =
            FirebaseAuth.getInstance().currentUser?.displayName
        view.findViewById<TextView>(R.id.tv_user_email).text =
            FirebaseAuth.getInstance().currentUser?.email
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_signOut -> {
                    performSignOut()
                }
                else -> false
            }
        }

    }

    private fun performSignOut(): Boolean {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(BookListingActivity@ this, CheckSessionActivity::class.java))
        finish()
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    private fun getDataSet(): ArrayList<BookInfo> {
        val dataSet: ArrayList<BookInfo> = java.util.ArrayList()
        dataSet.add(
            BookInfo(
                "In Search of Lost Time",
                "30.5",
                R.drawable.lost_book,
                "Marcel Proust",
                null,
                "Swann's Way, the first part of A la recherche de temps perdu, Marcel Proust's seven-part cycle, was published in 1913. In it, Proust introduces the themes that run through the entire work. The narrator recalls his childhood, aided by the famous madeleine; and describes M. Swann's passion for Odette. The work is incomparable. Edmund Wilson said \"[Proust] has supplied for the first time in literature an equivalent in the full scale for the new theory of modern physics."
            )
        )
        dataSet.add(
            BookInfo(
                "The Great Gatsby",
                "15",
                R.drawable.great_gatsby,
                "F. Scott Fitzgerald",
                null,
                "The novel chronicles an era that Fitzgerald himself dubbed the \"Jazz Age\". Following the shock and chaos of World War I, American society enjoyed unprecedented levels of prosperity during the \"roaring\" 1920s as the economy soared. At the same time, Prohibition, the ban on the sale and manufacture of alcohol as mandated by the Eighteenth Amendment, made millionaires out of bootleggers and led to an increase in organized crime, for example the Jewish mafia. Although Fitzgerald, like Nick Carraway in his novel, idolized the riches and glamor of the age, he was uncomfortable with the unrestrained materialism and the lack of morality that went with it, a kind of decadence."
            )
        )
        dataSet.add(
            BookInfo(
                "One Hundred Years of Solitude",
                "20",
                R.drawable.one_hundred_years,
                "Gabriel Garcia Marquez",
                null,
                "One of the 20th century's enduring works, One Hundred Years of Solitude is a widely beloved and acclaimed novel known throughout the world, and the ultimate achievement in a Nobel Prize–winning career. The novel tells the story of the rise and fall of the mythical town of Macondo through the history of the Buendía family. It is a rich and brilliant chronicle of life and death, and the tragicomedy of humankind. In the noble, ridiculous, beautiful, and tawdry story of the Buendía family, one sees all of humanity, just as in the history, myths, growth, and decay of Macondo, one sees all of Latin America. Love and lust, war and revolution, riches and poverty, youth and senility — the variety of life, the endlessness of death, the search for peace and truth — these universal themes dominate the novel. Whether he is describing an affair of passion or the voracity of capitalism and the corruption of government, Gabriel García Márquez always writes with the simplicity, ease, andpurity that are the mark of a master. Alternately reverential and comical, One Hundred Years of Solitude weaves the political, personal, and spiritual to bring a new consciousness to storytelling. Translated into dozens of languages, this stunning work is no less than an accounting of the history of the human race."
            )
        )
        dataSet.add(
            BookInfo(
                "War and Peace",
                "25",
                R.drawable.war_and_peace,
                "Leo Tolstoy",
                null,
                "Epic in scale, War and Peace delineates in graphic detail events leading up to Napoleon's invasion of Russia, and the impact of the Napoleonic era on Tsarist society, as seen through the eyes of five Russian aristocratic families."
            )
        )
        dataSet.add(
            BookInfo(
                "Lolita",
                "25",
                R.drawable.lolita,
                "Vladimir Nabokov",
                null,
                "The book is internationally famous for its innovative style and infamous for its controversial subject: the protagonist and unreliable narrator, middle aged Humbert Humbert, becomes obsessed and sexually involved with a twelve-year-old girl named Dolores Haze."
            )
        )
        val book = BookInfo(
            "Hamlet",
            "30",
            R.drawable.hamlet,
            "William Shakespeare",
            null,
            "The Tragedy of Hamlet, Prince of Denmark, or more simply Hamlet, is a tragedy by William Shakespeare, believed to have been written between 1599 and 1601. The play, set in Denmark, recounts how Prince Hamlet exacts revenge on his uncle Claudius, who has murdered Hamlet's father, the King, and then taken the throne and married Gertrude, Hamlet's mother. The play vividly charts the course of real and feigned madness—from overwhelming grief to seething rage—and explores themes of treachery, revenge, incest, and moral corruption."
        )
        book.rating = 3.0F
        dataSet.add(book)
        return dataSet
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
