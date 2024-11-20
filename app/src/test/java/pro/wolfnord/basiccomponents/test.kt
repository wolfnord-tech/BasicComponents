package com.example.kotlinlab7



import android.widget.EditText
import android.widget.ImageView
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import pro.wolfnord.basiccomponents.MainActivity
import java.lang.Thread.sleep


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class test {

    private lateinit var activity: MainActivity
    private lateinit var editText: EditText
    private lateinit var imageView: ImageView

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        editText = activity.findViewById(R.id.editTextText)
        imageView = activity.findViewById(R.id.imageView)
    }

    @Test
    fun testGoodURl(){
        Assert.assertEquals(true,activity.cool_URL("url.jpg"))
    }
    @Test
    fun testBadURl(){
        Assert.assertEquals(false,activity.cool_URL("url.jp"))
    }
    @Test
    fun testCleanEditText(){
        editText.setText("Привет")
        activity.clean_editText()
        Assert.assertEquals("",editText.text.toString())
    }
    @Test
    fun testFirstCoroutinesIsStart(){
        val url = "https://images.wallpaperscraft.com/image/single/drop_dew_water_1332324_1280x720.jpg"
        activity.main_finctional(url)
        sleep(5000)
        val logMessageGood = (ShadowLog.getLogsForTag("Карутина1").lastOrNull())?.msg?.contains("Карутина1 запущена")
        Assert.assertEquals(true, logMessageGood)
    }

    @Test
    fun mainFunctionalTest_InvalidURL(){
        val url = "https://example.com/image.png"
        activity.main_finctional(url)
        val logMessageBad = (ShadowLog.getLogsForTag("Фотка").lastOrNull())?.msg?.contains("Неправильное расширение")
        Assert.assertEquals(false, logMessageBad)
    }
}
