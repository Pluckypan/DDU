<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
                name="viewModel"
                type="$currentPackage$.$moduleName$Contract.IViewModel" />

        <variable
                name="view"
                type="$currentPackage$.$moduleName$Contract.IView" />

    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:background="@android:color/white">

        <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textColor="#333"
                android:textSize="14sp"
                app:easyJobData="@{viewModel.easyJobData}"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                tools:text="Take it easy &amp; Make it happen" />

        <androidx.constraintlayout.widget.Guideline
                android:id="@+id/guideLine"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                app:layout_constraintGuide_percent="0.5" />

        <Button
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="20dp"
                android:background="#333"
                android:onClick="@{view::toast}"
                android:paddingStart="20dp"
                android:paddingEnd="20dp"
                android:text="Hello"
                android:textAllCaps="false"
                android:textColor="@android:color/white"
                android:textSize="14sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toStartOf="@+id/guideLine"
                tools:ignore="HardcodedText" />


        <Button
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="20dp"
                android:background="#333"
                android:onClick="@{()->viewModel.calculate(8)}"
                android:paddingStart="20dp"
                android:paddingEnd="20dp"
                android:text="Jobs"
                android:textAllCaps="false"
                android:textColor="@android:color/white"
                android:textSize="14sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toEndOf="@+id/guideLine"
                tools:ignore="HardcodedText" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>