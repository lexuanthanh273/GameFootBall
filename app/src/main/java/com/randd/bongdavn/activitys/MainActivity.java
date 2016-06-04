package com.randd.bongdavn.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.randd.bongdavn.R;
import com.randd.bongdavn.models.Case;
import com.randd.bongdavn.models.Question;
import com.randd.bongdavn.sqlite.DBManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SCORE = "score";
    private Context mContext = MainActivity.this;

    private Button btQuestion1;
    private Button btQuestion2;
    private Button btQuestion3;
    private ProgressBar progressBar;
    private TextView tvTime;

    private TextView tvQuestion;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView ivResult1;
    private ImageView ivResult2;
    private ImageView ivResult3;
    private ImageView ivContent1;
    private ImageView ivContent2;
    private ImageView ivContent3;
    private Button btDelete1;
    private Button btDelete2;
    private Button btDelete3;
    private Button btKetThuc;

    private LinearLayout caseHorizontalScroll;
    private ArrayList<Case> listCase = new ArrayList<>();

    private boolean isAddIV1;
    private boolean isAddIV2;
    private boolean isAddIV3;
    private int idIV1;
    private int idIV2;
    private int idIV3;


    private CircleImageView ivRemove1;
    private CircleImageView ivRemove2;
    private CircleImageView ivRemove3;

    private RelativeLayout layout1;
    private RelativeLayout layout2;
    private RelativeLayout layout3;

    private int time = 15;
    private int questionCurrent = 1;
    private int score = 0;
    private boolean result1 = false, result2 = false, result3 = false;
    private boolean isDelete1 = true;
    private boolean isDelete2 = true;
    private boolean isDelete3 = true;
    private boolean dm = false;
    private boolean isQuestion2;
    private CheckTime checkTime;


    //ArrayList
    private ArrayList<Question> listQuestions = new ArrayList<>();

    //Manager
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(checkTime != null)
            checkTime = null;
        time = 15;
        progressBar.setProgress(time);
        score = 0;
        questionCurrent = 1;
        result1  = false;
        result2  = false;
        result3  = false;
        listQuestions.clear();
        listCase.clear();
        caseHorizontalScroll.removeAllViews();
        dbManager = new DBManager(mContext);
        listQuestions = dbManager.getQuestions();
        showNextQuestion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkTime.cancel(true);
        checkTime = null;
    }

    private void initViews() {

        btQuestion1 = (Button) findViewById(R.id.bt_question1);
        btQuestion2 = (Button) findViewById(R.id.bt_question2);
        btQuestion3 = (Button) findViewById(R.id.bt_question3);
        btKetThuc   = (Button) findViewById(R.id.bt_ket_thuc);

        btQuestion1.setOnClickListener(this);
        btQuestion2.setOnClickListener(this);
        btQuestion3.setOnClickListener(this);
        btKetThuc.setOnClickListener(this);
        btKetThuc.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvTime = (TextView) findViewById(R.id.tv_time);

        tvQuestion = (TextView) findViewById(R.id.tv_question_name);
        iv1 = (ImageView) findViewById(R.id.image1);
        iv2 = (ImageView) this.findViewById(R.id.image2);
        iv3 = (ImageView) this.findViewById(R.id.image3);
        btDelete1 = (Button) this.findViewById(R.id.bt_delete1);
        btDelete2 = (Button) this.findViewById(R.id.bt_delete2);
        btDelete3 = (Button) this.findViewById(R.id.bt_delete3);
        ivResult1 = (ImageView) this.findViewById(R.id.iv_result1);
        ivResult2 = (ImageView) this.findViewById(R.id.iv_result2);
        ivResult3 = (ImageView) this.findViewById(R.id.iv_result3);
        ivContent1 = (ImageView) this.findViewById(R.id.image_content1);
        ivContent2 = (ImageView) this.findViewById(R.id.image_content2);
        ivContent3 = (ImageView) this.findViewById(R.id.image_content3);
        ivContent1.setOnClickListener(this);
        ivContent2.setOnClickListener(this);
        ivContent3.setOnClickListener(this);
        caseHorizontalScroll = (LinearLayout) this.findViewById(R.id.case_horizontal_scrollview);

        ivResult1.setVisibility(View.INVISIBLE);
        ivResult2.setVisibility(View.INVISIBLE);
        ivResult3.setVisibility(View.INVISIBLE);
        btDelete1.setVisibility(View.INVISIBLE);
        btDelete2.setVisibility(View.INVISIBLE);
        btDelete3.setVisibility(View.INVISIBLE);

        btDelete1.setOnClickListener(this);
        btDelete2.setOnClickListener(this);
        btDelete3.setOnClickListener(this);

        layout1 = (RelativeLayout) findViewById(R.id.layout_1);
        layout2 = (RelativeLayout) findViewById(R.id.layout_2);
        layout3 = (RelativeLayout) findViewById(R.id.layout_3);

    }

    private void addImageView(final LinearLayout layout, final byte[] image, final int ID) {

        final CircleImageView imageView = new CircleImageView(this);
        final Bitmap bmImage = getCroppedBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        final LinearLayout.LayoutParams viewParamsCenter = new LinearLayout.LayoutParams(200, 200);
        viewParamsCenter.leftMargin = 32;
        viewParamsCenter.rightMargin = 32;
        imageView.setLayoutParams(viewParamsCenter);
        imageView.setBorderWidth(5);
        imageView.setBorderColorResource(R.color.pruple_500);
        imageView.setImageBitmap(bmImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAddIV1) {
                    ivContent1.setImageBitmap(bmImage);
                    btDelete1.setVisibility(View.VISIBLE);
                    ivRemove1 = imageView;
                    layout.removeView(imageView);
                    isAddIV1 = true;
                    isDelete1 = false;
                    idIV1 = ID;
                    checkResult();
                } else {
                    if (!isAddIV2) {
                        ivContent2.setImageBitmap(bmImage);
                        btDelete2.setVisibility(View.VISIBLE);
                        ivRemove2 = imageView;
                        layout.removeView(imageView);
                        isAddIV2 = true;
                        isDelete2 = false;
                        idIV2 = ID;
                        checkResult();
                    } else {
                        if (isAddIV3)
                            return;
                        ivContent3.setImageBitmap(bmImage);
                        btDelete3.setVisibility(View.VISIBLE);
                        ivRemove3 = imageView;
                        layout.removeView(imageView);
                        isAddIV3 = true;
                        isDelete3 = false;
                        idIV3 = ID;
                        checkResult();
                    }
                }

            }
        });
        layout.addView(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete1:
                deleteIVContent1();
                break;
            case R.id.bt_delete2:
                deleteIVContent2();
                break;
            case R.id.bt_delete3:
                deleteIVContent3();
                break;
            case R.id.image_content1:
                if (!result1 && !isDelete1) {
                    deleteIVContent1();
                    isDelete1 = true;
                }
                break;
            case R.id.image_content2:
                if (!result2 && !isDelete2) {
                    deleteIVContent2();
                    isDelete2 = true;
                }
                break;
            case R.id.image_content3:
                if (!result3 && !isDelete3) {
                    deleteIVContent3();
                    isDelete3 = true;
                }
                break;
            case R.id.bt_question2:
//                if (!isQuestion2) {
//                    startAnimationAllView();
//                    questionCurrent++;
//                    time = 0;
//                    showNextQuestion();
//                    endAnimationAllView();
//                    isQuestion2 = true;
//                }
                break;
            case R.id.bt_ket_thuc:
                time = 0;
                checkTime.onPostExecute(null);
                break;
        }
    }

    private void checkResult() {
        if (isAddIV1 && isAddIV2 && isAddIV3) {
            ivResult1.setVisibility(View.VISIBLE);
            ivResult2.setVisibility(View.VISIBLE);
            ivResult3.setVisibility(View.VISIBLE);
//            if (questionCurrent > 1)
//                btKetThuc.setVisibility(View.VISIBLE);
            String[] resultSQL = listQuestions.get(questionCurrent - 1).getTrueCase().split(",");

            int[] arr1 = {Integer.parseInt(resultSQL[0]), Integer.parseInt(resultSQL[1]), Integer.parseInt(resultSQL[2])};
            for (int anArr1 : arr1) {
                if (idIV1 == anArr1) {
                    iv1.setImageResource(R.drawable.dung);
                    ivResult1.setImageResource(R.drawable.ic_true);
                    btDelete1.setVisibility(View.INVISIBLE);
                    result1 = true;
                    break;
                }
            }
            if (!result1) {
                iv1.setImageResource(R.drawable.sai);
                ivResult1.setImageResource(R.drawable.ic_wrong);
            }
            for (int anArr2 : arr1) {
                if (idIV2 == anArr2) {
                    iv2.setImageResource(R.drawable.dung);
                    ivResult2.setImageResource(R.drawable.ic_true);
                    btDelete2.setVisibility(View.INVISIBLE);
                    result2 = true;
                    break;
                }
            }
            if (!result2) {
                iv2.setImageResource(R.drawable.sai);
                ivResult2.setImageResource(R.drawable.ic_wrong);
            }
            for (int anArr3 : arr1) {
                if (idIV3 == anArr3) {
                    iv3.setImageResource(R.drawable.dung);
                    ivResult3.setImageResource(R.drawable.ic_true);
                    btDelete3.setVisibility(View.INVISIBLE);
                    result3 = true;
                    break;
                }
            }
            if (!result3) {
                iv3.setImageResource(R.drawable.sai);
                ivResult3.setImageResource(R.drawable.ic_wrong);
            }
        }
    }

    private class CheckTime extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            time = 15;
            progressBar.setMax(time);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (time >= 0) {
                SystemClock.sleep(1000);
                publishProgress(time);
                time--;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            assert progressBar != null;
            progressBar.setProgress(values[0]);
            tvTime.setText(values[0] + "");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            checkResult();

            if (result1)
                score++;
            if (result2)
                score++;
            if (result3)
                score++;
            if (questionCurrent == 2) {
                if (!dm) {
                    dm = true;
                    Log.e("FUCK FUCK", "FUCK END");
                    Intent intent = new Intent(MainActivity.this, FinishActivity.class);
                    intent.putExtra(SCORE, score);
                    startActivity(intent);

                    return;
                }
            }
            startAnimationAllView();
            Log.e("FUCK FUCK", "FUCK CUOI CUNG");

            questionCurrent++;
            showNextQuestion();
            endAnimationAllView();
            isQuestion2 = true;
            super.onPostExecute(aVoid);

        }
    }

    private Bitmap getCroppedBitmap(Bitmap bitmap) {

        int targetWidth = 200;
        int targetHeight = 200;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);

        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawBitmap(bitmap,
                new Rect(0, 0, bitmap.getWidth(),
                        bitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), paint);
        return targetBitmap;
    }

    private void startAnimationView(View view, int animation) {
        Animation animation1 = AnimationUtils.loadAnimation(MainActivity.this, animation);
        view.startAnimation(animation1);
    }

    private void startAnimationAllView() {
        startAnimationView(layout1, R.anim.scale);
        startAnimationView(layout2, R.anim.scale);
        startAnimationView(layout3, R.anim.scale);
        startAnimationView(tvQuestion, R.anim.scale);
        startAnimationView(caseHorizontalScroll, R.anim.scale);
        if (questionCurrent == 1) {
            startAnimationView(btQuestion1, R.anim.scale);
            startAnimationView(btQuestion2, R.anim.scale);
        }
//        if (questionCurrent == 2) {
//            startAnimationView(btQuestion2, R.anim.scale);
//            startAnimationView(btQuestion3, R.anim.scale);
//        }
    }

    private void endAnimationAllView() {
        startAnimationView(layout1, R.anim.scale2);
        startAnimationView(layout2, R.anim.scale2);
        startAnimationView(layout3, R.anim.scale2);
        startAnimationView(tvQuestion, R.anim.scale2);
        startAnimationView(caseHorizontalScroll, R.anim.scale2);
        if (questionCurrent == 2) {
            btQuestion1.setBackgroundResource(R.drawable.cau1);
            btQuestion2.setBackgroundResource(R.drawable.cau2_active);
            startAnimationView(btQuestion1, R.anim.scale2);
            startAnimationView(btQuestion2, R.anim.scale2);
        }
        if (questionCurrent == 3) {
            btQuestion2.setBackgroundResource(R.drawable.cau2);
            btQuestion3.setBackgroundResource(R.drawable.cau3_active);
            startAnimationView(btQuestion2, R.anim.scale2);
            startAnimationView(btQuestion3, R.anim.scale2);
        }
    }

    private void showNextQuestion() {
        tvQuestion.setText(listQuestions.get(questionCurrent - 1).getQuestionName());
        listCase = dbManager.getCases(listQuestions.get(questionCurrent - 1).getID());
        caseHorizontalScroll.removeAllViews();
        resetAllView();
        for (int i = 0; i < listCase.size(); i++) {
            addImageView(caseHorizontalScroll, listCase.get(i).getCaseImage(), listCase.get(i).getID());
        }
    }

    private void resetAllView() {
        iv1.setImageResource(R.drawable.chosse_people); iv2.setImageResource(R.drawable.chosse_people); iv3.setImageResource(R.drawable.chosse_people);
        ivContent1.setImageBitmap(null); ivContent2.setImageBitmap(null); ivContent3.setImageBitmap(null);
        btDelete1.setVisibility(View.INVISIBLE); btDelete2.setVisibility(View.INVISIBLE); btDelete3.setVisibility(View.INVISIBLE);
        ivResult1.setVisibility(View.INVISIBLE); ivResult2.setVisibility(View.INVISIBLE); ivResult3.setVisibility(View.INVISIBLE);
        isAddIV1 = false; isAddIV2 = false; isAddIV3 = false;
        result1  = false; result2  = false; result3  = false;
        checkTime = new CheckTime();
        checkTime.execute();

    }

    public void updatePoi() {
        if (checkTime.getStatus() == AsyncTask.Status.RUNNING || checkTime.getStatus() == AsyncTask.Status.PENDING) {
            checkTime.cancel(true);
            checkTime = new CheckTime();
        } else {
            checkTime = new CheckTime();
        }
        checkTime.execute();
    }

    private void deleteIVContent1() {
        iv1.setImageResource(R.drawable.chosse_people);
        ivContent1.setImageBitmap(null);
        ivResult1.setVisibility(View.INVISIBLE);
        btDelete1.setVisibility(View.INVISIBLE);

        if (!result2) {
            iv2.setImageResource(R.drawable.chosse_people);
            ivResult2.setVisibility(View.INVISIBLE);
        }
        if (!result3) {
            iv3.setImageResource(R.drawable.chosse_people);
            ivResult3.setVisibility(View.INVISIBLE);
        }
        caseHorizontalScroll.addView(ivRemove1, 0);
        isAddIV1 = false;
    }
    private void deleteIVContent2() {
        if (!result1) {
            iv1.setImageResource(R.drawable.chosse_people);
            ivResult1.setVisibility(View.INVISIBLE);
        }
        iv2.setImageResource(R.drawable.chosse_people);
        ivContent2.setImageBitmap(null);
        btDelete2.setVisibility(View.INVISIBLE);
        ivResult2.setVisibility(View.INVISIBLE);
        if (!result3) {
            iv3.setImageResource(R.drawable.chosse_people);
            ivResult3.setVisibility(View.INVISIBLE);
        }
        caseHorizontalScroll.addView(ivRemove2, 0);
        isAddIV2 = false;
    }
    private void deleteIVContent3() {
        if (!result1) {
            iv1.setImageResource(R.drawable.chosse_people);
            ivResult1.setVisibility(View.INVISIBLE);
        }
        if (!result2) {
            iv2.setImageResource(R.drawable.chosse_people);
            ivResult2.setVisibility(View.INVISIBLE);
        }
        iv3.setImageResource(R.drawable.chosse_people);
        ivContent3.setImageBitmap(null);
        btDelete3.setVisibility(View.INVISIBLE);
        ivResult3.setVisibility(View.INVISIBLE);
        caseHorizontalScroll.addView(ivRemove3, 0);
        isAddIV3 = false;
    }
}
