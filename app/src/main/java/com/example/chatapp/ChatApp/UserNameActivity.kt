import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivityUserNameBinding

class UserNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserNameBinding
    private var crossUsn: String? = null // Declare crossUsn as a class-level variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null && intent.hasExtra("username")) {
            crossUsn = intent.getStringExtra("username");
        }

        // Set the text of the EditText to the value of crossUsn
        binding.etUsername.setText(crossUsn)

        // Disable the EditText so that the user cannot modify it
        binding.etUsername.isEnabled = false

        // Hide the Proceed button since there's no input needed
        binding.btnProceed.visibility = View.GONE
    }
}
