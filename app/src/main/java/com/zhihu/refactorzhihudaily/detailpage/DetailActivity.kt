package com.zhihu.refactorzhihudaily.detailpage

import WebPageAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.zhihu.refactorzhihudaily.R
import com.zhihu.refactorzhihudaily.model.Moduel.sampleNews
import com.zhihu.refactorzhihudaily.model.News
import com.zhihu.refactorzhihudaily.model.RemixItem
import com.zhihu.refactorzhihudaily.detailpage.DetailImplementation.getTheBeforeNewsList
import com.zhihu.refactorzhihudaily.mainpage.MainPreImple.remixList
import com.zhihu.refactorzhihudaily.mainpage.MainPreImple.topNewsList
import org.jetbrains.anko.find

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val page = find<ViewPager>(R.id.page)
        var type = intent.getIntExtra("type",3)
        var id = intent.getIntExtra("newsId",0)

        if (type == 1){
            var currentNews : News = sampleNews
            val pageList = ArrayList<WebView>()
            topNewsList.forEach {
                DetailImplementation.addView(it.id,pageList,this@DetailActivity)
                if (it.id == id){
                    currentNews = it
                }
            }
            val index : Int = topNewsList.indexOf(currentNews)
            page.adapter = WebPageAdapter(pageList)
            page.setCurrentItem(index)
        }else{
            var currentNews : RemixItem? = null
            val pageList = ArrayList<WebView>()
            remixList!!.forEach {
                if (it.id!=null&&it.id!=0){
                    DetailImplementation.addView(it.id!!,pageList,this@DetailActivity)
                    if (it.id == id){
                        currentNews = it
                    }
                }
            }
            val index : Int = getIndex(remixList,currentNews!!)
            var adapter = WebPageAdapter(pageList)
            page.adapter = adapter
            page.setCurrentItem(index-1)
            page.addOnPageChangeListener(object :OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (position==pageList.size-1){
                        //滑到最后一页时加载下一天的新闻
                        getTheBeforeNewsList(adapter,pageList,this@DetailActivity)
                    }
                }

            })

        }


    }


    //获取正确的current position
    fun getIndex(remixList: MutableList<RemixItem>,currentNews:RemixItem):Int{
        var index : Int = 0
        for (i:Int in 0..remixList.indexOf(currentNews))
            if (remixList.get(i).id!=null&&remixList.get(i).id != 0){
                index++
            }

        return index
    }
}
