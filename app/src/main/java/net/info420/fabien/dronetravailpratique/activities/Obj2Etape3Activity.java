package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.info420.fabien.dronetravailpratique.R;

import org.opencv.core.Mat;

public class Obj2Etape3Activity extends AppCompatActivity {
  public static final String TAG = Obj2Etape3Activity.class.getName();

  private TextView  tvImage;
  private ImageView ivImage;
  private Button    btnResultat;
  private Button    btnOriginal;

  private Mat image;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    initUI();
  }

  private void initUI(){
    setContentView(R.layout.obj2_etape3_activity);

    tvImage = (TextView) findViewById(R.id.tv_image);
    ivImage = (ImageView) findViewById(R.id.iv_image);
    btnResultat = (Button) findViewById(R.id.btn_resultat);
    btnOriginal = (Button) findViewById(R.id.btn_original);

    btnResultat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        operate();
      }
    });

    btnOriginal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        resetUI();
      }
    });

    resetUI();
  }

  private void resetUI() {
    tvImage.setText(getString(R.string.original_image));

    btnOriginal.setEnabled(false);
    btnResultat.setEnabled(true);

    ivImage.setImageBitmap(null);

    // TODO : Mettre une image
    // ivImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.man));
  }

  private void operate() {
    // TODO : L'opération

    tvImage.setText(getString(R.string.resultat_image));
    btnOriginal.setEnabled(true);
    btnResultat.setEnabled(false);
  }
}
