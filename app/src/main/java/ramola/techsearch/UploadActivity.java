package ramola.techsearch;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

import static ramola.techsearch.Field.BASE_URL;
import static ramola.techsearch.Field.URL_UPLOAD;


public class UploadActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextInputLayout category_layout,title_layout,short_layout,long_layout;
 private EditText category,title,short_description,long_description;
 private ImageView imageView;
 private Button upload,save;
    private String encodedString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        category_layout= (TextInputLayout) findViewById(R.id.category_upload_layout);
        title_layout= (TextInputLayout) findViewById(R.id.title_upload_layout);
        short_layout= (TextInputLayout) findViewById(R.id.short_upload_layout);
        long_layout= (TextInputLayout) findViewById(R.id.long_upload_layout);

        category= (EditText) findViewById(R.id.category_upload);
        title= (EditText) findViewById(R.id.title_upload);
        short_description= (EditText) findViewById(R.id.short_upload);
        long_description= (EditText) findViewById(R.id.long_upload);

        imageView= (ImageView) findViewById(R.id.image_upload);

        upload= (Button) findViewById(R.id.save_content_upload);
        save= (Button) findViewById(R.id.btn_image_upload);

        category.addTextChangedListener(new MyTextWatcher(category));
        title.addTextChangedListener(new MyTextWatcher(title));
        short_description.addTextChangedListener(new MyTextWatcher(short_description));
        long_description.addTextChangedListener(new MyTextWatcher(long_description));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createchooser();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendJson();
            }
        });

    }

    private boolean checkEmpty(EditText view){
        return view.getText().toString().trim().isEmpty();
    }
private void ShowError(View v){
    switch (v.getId()){
        case R.id.category_upload:
            category_layout.setError("ENTER THE CATEGORY");
            break;
        case R.id.title_upload:
            title_layout.setError("ENTER THE TITLE");
            break;
        case R.id.short_upload:
            short_layout.setError("ENTER THE SHORT DESCRIPTION");
            break;
        case R.id.long_upload:
            long_layout.setError("ENTER THE LONG DESCRIPTION");
            break;

    }
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(filePath, filePathColumn, null, null, null);
            c.moveToFirst();
            String imgDecodableString = c.getString(c.getColumnIndex(filePathColumn[0]));
            c.close();
            Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
            imageView.setImageBitmap(bitmap);
            encodedString=decodeImage(bitmap);
        }
    }

    private String decodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("DECODE IMAGE",encodedImage);
        return encodedImage;
    }

    private void createchooser(){
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "CHOOSE PHOTO"), PICK_IMAGE_REQUEST);
    }
    private void DoTask(EditText v){
        if(checkEmpty(v))
            ShowError(v);
    }
    private class MyTextWatcher implements TextWatcher{
        private EditText view;

        private MyTextWatcher(EditText view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
          DoTask(view);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SendJson(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,getUrl(),new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(UploadActivity.this,"UPLOADED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UploadActivity.this," UNSUCCESSFULLY",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> hMap=new Hashtable<>();
                hMap.put("category",category.getText().toString());
                hMap.put("title",title.getText().toString());
                hMap.put("short_description",short_description.getText().toString());
                hMap.put("long_description",long_description.getText().toString());
                hMap.put("photo",encodedString);
                return hMap;
            }
        };
        MySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(stringRequest);
    }

    private String getUrl() {
        return BASE_URL+URL_UPLOAD;
    }
}
