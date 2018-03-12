package com.example.user.eventest.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by DR
 * on 12.03.2018.
 */
@Entity
class Attachments(@PrimaryKey(autoGenerate = true)
                  var attachID: Long = 0,
                  var memoID: Long,
                  var pathToAttach: String?)