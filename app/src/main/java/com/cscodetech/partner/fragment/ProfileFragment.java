package com.cscodetech.partner.fragment;

import static com.cscodetech.partner.utils.FileUtils.isLocal;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cscodetech.partner.R;
import com.cscodetech.partner.imagepicker.ImagePicker;
import com.cscodetech.partner.model.LoginUser;
import com.cscodetech.partner.model.User;
import com.cscodetech.partner.retrofit.APIClient;
import com.cscodetech.partner.retrofit.GetResult;
import com.cscodetech.partner.utils.CustPrograssbar;
import com.cscodetech.partner.utils.FileUtils;
import com.cscodetech.partner.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment implements GetResult.MyListener {

    @BindView(R.id.ed_firstname)
    EditText edFirstname;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.ed_address)
    EditText edAddress;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_service)
    EditText edService;
    @BindView(R.id.ed_bio)
    EditText edBio;
    @BindView(R.id.btn_countinue)
    TextView btnCountinue;
    @BindView(R.id.img_profile)
    CircleImageView imgProfile;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;
    public ImagePicker imagePicker;

    public static ProfileFragment profileFragment;

    public static ProfileFragment getInstance() {
        return profileFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        profileFragment = this;
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 1);
        imagePicker = new ImagePicker();

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        edFirstname.setText("" + user.getName());
        edEmail.setText("" + user.getEmail());
        edMobile.setText("" + user.getMobile());
        edAddress.setText("" + user.getAddress());
        edPassword.setText("" + user.getPassword());
        edService.setText("" + user.getCategory());
        edBio.setText("" + user.getBoi());
        Glide.with(getActivity()).load(APIClient.baseUrl + "/"+user.getPimg()).into(imgProfile);


        return view;
    }

    public void bottonChoseoption() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());

        View sheetView = getLayoutInflater().inflate(R.layout.activity_image_select, null);
        mBottomSheetDialog.setContentView(sheetView);

        TextView textViewCamera = sheetView.findViewById(R.id.textViewCamera);
        TextView textViewGallery = sheetView.findViewById(R.id.textViewGallery);
        TextView textViewCancel = sheetView.findViewById(R.id.textViewCancel);


        mBottomSheetDialog.show();

        textViewCamera.setOnClickListener(v -> {

            mBottomSheetDialog.cancel();
            imagePicker.withActivity(getActivity()).chooseFromGallery(false).chooseFromCamera(true).withCompression(true).start();


        });
        textViewGallery.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            imagePicker.withActivity(getActivity()).chooseFromGallery(true).chooseFromCamera(false).withCompression(true).start();

        });
        textViewCancel.setOnClickListener(v -> mBottomSheetDialog.cancel());
    }

    private void uploadMultiFile(String filePaths) {
        custPrograssbar.prograssCreate(getActivity());
        List<MultipartBody.Part> parts = new ArrayList<>();

        if (filePaths != null) {
            // create part for file (photo, video, ...)

            parts.add(prepareFilePart("image0", filePaths));

        }
// create a map of data to pass along

        RequestBody uid = createPartFromString(user.getId());
        RequestBody name = createPartFromString(edFirstname.getText().toString());
        RequestBody password = createPartFromString(edPassword.getText().toString());
        RequestBody address = createPartFromString(edAddress.getText().toString());
        RequestBody bio = createPartFromString(edBio.getText().toString());
        RequestBody size = createPartFromString("" + parts.size());

// finally, execute the request
        Call<JsonObject> call = APIClient.getInterface().uploadMultiFile(uid, name, password, address, bio, size, parts);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                custPrograssbar.closePrograssBar();
                Gson gson = new Gson();

                LoginUser loginUser = gson.fromJson(response.body(), LoginUser.class);
                Toast.makeText(getActivity(), loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    User user = loginUser.getUser();
                    user.setCategory(loginUser.getCategory());
                    sessionManager.setUserDetails("", user);
                    user = sessionManager.getUserDetails("");
                    edFirstname.setText("" + user.getName());
                    edEmail.setText("" + user.getEmail());
                    edMobile.setText("" + user.getMobile());
                    edAddress.setText("" + user.getAddress());
                    edPassword.setText("" + user.getPassword());
                    edBio.setText("" + user.getBoi());
                    Glide.with(getActivity()).load(APIClient.baseUrl + "/"+user.getPimg()).into(imgProfile);

                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                custPrograssbar.closePrograssBar();

            }
        });
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_TEXT), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {
        // use the FileUtils to get the actual file by uri
        File file = getFile( fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static File getFile(String path) {
        if (path == null) {
            return null;
        }

        if (isLocal(path)) {
            return new File(path);
        }
        return null;
    }


    @OnClick({R.id.btn_countinue, R.id.lvl_edit})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_countinue) {
            if(validation()){
                uploadMultiFile(imagePath);
            }
        } else if (view.getId() == R.id.lvl_edit) {
             bottonChoseoption();

        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                LoginUser loginUser = gson.fromJson(result.toString(), LoginUser.class);
                Toast.makeText(getActivity(), loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    User user = loginUser.getUser();
                    user.setCategory(loginUser.getCategory());
                    sessionManager.setUserDetails("", user);
                    user = sessionManager.getUserDetails("");
                    edFirstname.setText("" + user.getName());
                    edEmail.setText("" + user.getEmail());
                    edMobile.setText("" + user.getMobile());
                    edAddress.setText("" + user.getAddress());
                    edPassword.setText("" + user.getPassword());
                    edBio.setText("" + user.getBoi());
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());

        }
    }

    public boolean validation() {
        if (TextUtils.isEmpty(edFirstname.getText().toString())) {
            edFirstname.setError("Enter Name");
            return false;
        }
        if (TextUtils.isEmpty(edPassword.getText().toString())) {
            edPassword.setError("Enter Password");
            return false;
        }
        if (TextUtils.isEmpty(edAddress.getText().toString())) {
            edAddress.setError("Enter Address");
            return false;
        }
        return true;
    }

    public void setProfile(String path) {
        imagePath = path;
        Glide.with(getActivity()).load(path).into(imgProfile);
    }

    String imagePath;

}