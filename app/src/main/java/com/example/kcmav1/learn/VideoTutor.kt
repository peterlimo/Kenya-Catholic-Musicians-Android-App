package com.example.kcmav1.learn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.Video
import com.example.kcmav1.recyclers.VideoTutorialsAdapter
import com.example.kcmav1.utils.toast
import com.google.firebase.firestore.FirebaseFirestore
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern


class VideoTutor : AppCompatActivity(),VideoTutorialsAdapter.OnItemClickListener {
    lateinit var adapter: VideoTutorialsAdapter
    val list=ArrayList<Video>()
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_tutor)
        recyclerView=findViewById(R.id.yt_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter= VideoTutorialsAdapter(list,this)
//        val image_thumb=findViewById<ImageView>(R.id.image_thumb)

//        Picasso.get().load(url).into(image_thumb)

  val  db:FirebaseFirestore= FirebaseFirestore.getInstance()
        db.collection("learn").document("videos")
            .collection("youtubeLinks")
            .addSnapshotListener{value,e->
                if (e!=null)
                {
                    return@addSnapshotListener
                }
                list.clear()
                for (doc in value!!)
                {
                  val url=doc.getString("link").toString()
                  val imageLink="https://i.ytimg.com/vi/"+getVideoId(url)+"/mqdefault.jpg"
//                    val title= getTitle(url)
                    val id=getVideoId(url)
                  list.add(Video(imageLink,"title",url,id.toString()))
                  recyclerView.adapter=adapter
                }
            }

    }
    private fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }
    fun getVideoId(videoUrl: String): String? {
        var videoId = ""
        val regex =
            "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)"
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(videoUrl)
        if (matcher.find()) {
            videoId = matcher.group(1)
        }
        return videoId
    }
//    fun getTitle(url:String):String{
//        val embeddedUrl=URL("http://www.youtube.com/oembed?url=$url&format=json")
//
//        return JSONObject(IOUtils.toString(embeddedUrl)).getString("title")
//    }

    override fun onItemClick(position: Int) {
        val item=list.get(position)
        val id=item.videoId
        toast(item.url)
       val i=Intent(applicationContext,PlayVideoActivity::class.java)
        i.putExtra("id",id)
        startActivity(i)
    }
}
//object SimpleYouTubeHelper {
//    fun getTitleQuietly(youtubeUrl: String?): String? {
//
//            if (youtubeUrl != null) {
//                val embededURL = URL(
//                    "http://www.youtube.com/oembed?url=" +
//                            youtubeUrl + "&format=json"
//                )
//                return JSONObject(IOUtils.toString(embededURL)).getString("title")
//            }
//
//        return null
//    }
//}
