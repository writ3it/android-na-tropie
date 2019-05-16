package pl.zhp.natropie.ui.PostLists

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import pl.zhp.natropie.db.entities.Post



import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import kotlinx.android.synthetic.main.post_excerpt_layout.view.*
import pl.zhp.natropie.R
import pl.zhp.natropie.db.entities.PostWithColor
import pl.zhp.natropie.ui.models.PostVM


class PostsAdapter(context: Context,val objects: MutableList<PostVM>) :
    ArrayAdapter<PostVM>(context, R.layout.post_excerpt_layout,R.id.row_post_title, objects), PostsListPresenter.UpdatetableAdapter {

    override fun showPostsList(_data: List<PostVM>) {
        objects.clear()
        objects.addAll(_data)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // @TODO: może idzie to jakoś prosto na bindingsy przerobić?
        val post = super.getItem(position)!!
        var view = super.getView(position, convertView, parent)
        view.row_post_category_name.text = post.category
        view.row_post_date_and_author.text = post.author_and_date
        view.row_post_title.text = post.title
        view.row_post_excerpt.text = post.excerpt
        val color = Color.parseColor(post.color)
        view.row_post_title.setBackgroundColor(color)
        view.row_post_category_name.setBackgroundColor(color)
        view.row_post_excerpt.setBackgroundColor(color)
        view.row_post_date_and_author.setBackgroundColor(color)
        view.row_post_read_more.setBackgroundColor(color)
        view.border1.setBackgroundColor(color)
        view.border2.setBackgroundColor(color)
        view.border3.setBackgroundColor(color)
        return view
    }

}