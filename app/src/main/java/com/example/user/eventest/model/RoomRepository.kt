package com.example.user.eventest.model

import android.content.Context
import android.os.AsyncTask
import com.example.user.eventest.eventbus.events.MemoAdapterRefreshEvent
import com.example.user.eventest.room.AppDatabase
import com.example.user.eventest.room.DateConverterDB
import org.greenrobot.eventbus.EventBus
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * Created by DR
 * on 28.02.2018.
 */

class RoomRepository(context: Context) : MemoRepository {
    private val db = AppDatabase.getInstance(context)

    override fun getConcreteMemo(memo: Memo): Memo {
        try {
            return GetConcreteMemoTask(memo, db)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, db).get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return memo
    }

    class GetConcreteMemoTask(val memo: Memo, val db: AppDatabase) : AsyncTask<AppDatabase, Void, Memo>() {
        private val dateConverter = DateConverterDB()

        override fun doInBackground(vararg v: AppDatabase): Memo {
            return db.memoDAO.getConcreteMemo(
                    dateConverter.stringFromDate(memo.date), memo.note)
        }
    }

    override fun getAllData(): ArrayList<Memo> {
        try {
            return GetAllMemos(db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, db).get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return ArrayList()
    }

    class GetAllMemos(val db: AppDatabase) : AsyncTask<AppDatabase, Void, ArrayList<Memo>>() {
        override fun doInBackground(vararg appDatabase: AppDatabase): ArrayList<Memo> {
            return ArrayList(db.memoDAO.getAllMemo())
        }
    }

    override fun addMemo(memo: Memo) {
        AddMemoTask(memo, db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    class AddMemoTask(val memo: Memo, val db: AppDatabase) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            db.memoDAO.insert(memo)
            return null
        }

        override fun onPostExecute(aVoid: Void) {
            super.onPostExecute(aVoid)
            EventBus.getDefault().post(MemoAdapterRefreshEvent())
        }
    }

    override fun deleteMemo(memo: Memo) {
        deleteByMemoID(memo.memoID)
        AsyncTask.execute({
            run {
                db.memoDAO.delete(memo)
            }
        })
    }

    @Override
    override fun deleteByMemoID(memoID: Long) {
        DeleteMemoByIDTask(memoID, db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    class DeleteMemoByIDTask(val id: Long, val db: AppDatabase) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg v: Void?): Void? {
            db.memoDAO.deleteByMemoId(id)
            return null
        }

        override fun onPostExecute(v: Void) {
            super.onPostExecute(v)
            EventBus.getDefault().post(MemoAdapterRefreshEvent())
        }
    }

    override fun updateMemo(memo: Memo) {
        UpdateMemoTask(memo, db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    class UpdateMemoTask(val memo: Memo, val db: AppDatabase) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg v: Void?): Void? {
            db.memoDAO.update(memo)
            return null
        }

        override fun onPostExecute(aVoid: Void) {
            super.onPostExecute(aVoid)
            EventBus.getDefault().post(MemoAdapterRefreshEvent())
        }
    }
}
