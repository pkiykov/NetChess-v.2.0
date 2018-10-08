package com.netchess.pkiykov.core

import com.google.firebase.database.DatabaseError

class FirebaseErrorWrapper(val error: DatabaseError) : Exception()