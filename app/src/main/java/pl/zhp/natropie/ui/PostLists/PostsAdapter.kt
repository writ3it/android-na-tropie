package pl.zhp.natropie.ui.PostLists

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import pl.zhp.natropie.R
import pl.zhp.natropie.db.entities.Post

class PostsAdapter(context: Context,val objects: MutableList<Post>) :
    ArrayAdapter<Post>(context, R.layout.post_excerpt_layout,R.id.row_post_title, objects), PostsListPresenter.UpdatetableAdapter {

    override fun showPostsList(_data: List<Post>) {
        objects.clear()
        objects.addAll(_data)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val post = super.getItem(position)!!
        var view = super.getView(position, convertView, parent)
        val titleView = view.findViewById<TextView>(R.id.row_post_title)
        titleView.text = post.title
        view.findViewById<TextView>(R.id.row_post_excerpt).text = post.excerpt
        return view
    }
}