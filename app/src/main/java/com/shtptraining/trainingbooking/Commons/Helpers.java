package com.shtptraining.trainingbooking.Commons;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Predicate;
import com.shtptraining.trainingbooking.Commons.MappingAddress.QuanHuyenPhuongXa;
import com.shtptraining.trainingbooking.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Helpers {
    public static AlertDialog _dialog;

    public static void showToast(Context context, String contentMsg, int duration) {
        Toast.makeText(context, contentMsg, duration).show();
    }

    public static <String> ArrayList<String> filterNamesByParentCode(ArrayList<QuanHuyenPhuongXa> col, Predicate<QuanHuyenPhuongXa> predicate) {
        ArrayList<String> result = new ArrayList<String>();
        for (QuanHuyenPhuongXa element : col) {
            if (predicate.apply(element)) {
                result.add((String) element.getName_with_type());
            }
        }
        return result;
    }

    public static <String> ArrayList<String> filterParentCodeByName(ArrayList<QuanHuyenPhuongXa> col, Predicate<QuanHuyenPhuongXa> predicate) {
        ArrayList<String> result = new ArrayList<String>();
        for (QuanHuyenPhuongXa element : col) {
            if (predicate.apply(element)) {
                result.add((String) element.getCode());
            }
        }
        return result;
    }


    public static void showAlertDialogExitApp(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((AppCompatActivity) context).finish();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage("Bạn có muốn thoát app?");
        builder.setTitle("Thoát app");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static AlertDialog showAlertDialogConfirmInfor(Context context, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);

        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static void showLoadingDialog(Context context, String content) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
        TextView tvContent = (TextView) v.findViewById(R.id.tv_content);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        builder.setCancelable(false);
        tvContent.setText(content);

        _dialog = builder.create();
        _dialog.show();
    }

    public static void dismissLoadingDialog() {
        if (_dialog != null) {
            _dialog.dismiss();
        }
    }

    public static String toDDMMYYY(String dateString) {
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date date = null;
        try {
            date = inputFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String outputText = outputFormat.format(date);
        return outputText;
    }
}
