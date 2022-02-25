package layout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nikitafe_oblig2.AlpacaPartyItem
import com.example.nikitafe_oblig2.R
import de.hdodenhof.circleimageview.CircleImageView

class PartyAdapter(
    val mutableParties: MutableList<AlpacaPartyItem>
): RecyclerView.Adapter<PartyAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return mutableParties.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //gaar gjennom hver alpakaparty i lista og bruker adapater for aa sette informasjonen i kort
        for(i in mutableParties){
            println(i)
            println("lol kekw vi går")
            i.let { holder.bind(mutableParties[position]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { //lager recyclerview viewholder

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.partycard, parent, false)
        return ViewHolder(inflater)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val alpacaBilde: CircleImageView = itemView.findViewById(R.id.partyImage)  //lagrer viewene i kortet
        private val alpacaNavn: TextView = itemView.findViewById(R.id.textViewName)
        private val alpacaLag: TextView = itemView.findViewById(R.id.textViewDescription)
        private val farge: View = itemView.findViewById(R.id.colorView)
        private val stemmer: TextView = itemView.findViewById(R.id.textViewVotes)

        fun bind(nyalp : AlpacaPartyItem){  //naar den faar alpakka objekt saa vil den fordele verdiene til viewene som tilhoerer den
            alpacaNavn.text = nyalp.leader
            alpacaLag.text = nyalp.name
            farge.setBackgroundColor(Color.parseColor(nyalp.color))
            stemmer.text = nyalp.voteText //nyalp.stemmetekst
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions).load(nyalp.img).into(alpacaBilde) //henter bildet fra nettet
        }

    }


}