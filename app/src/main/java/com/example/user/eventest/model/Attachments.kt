package com.example.user.eventest.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by DR
 * on 12.03.2018.
 */
@Entity(tableName = "attachments")
class Attachments(@PrimaryKey(autoGenerate = true)
                  @ColumnInfo(name = "attach_id")
                  var attachID: Long = 0,
                  @ColumnInfo(name = "memo_id")
                  var memoID: Long,
                  @ColumnInfo(name = "path_to_attach")
                  var pathToAttach: String?)