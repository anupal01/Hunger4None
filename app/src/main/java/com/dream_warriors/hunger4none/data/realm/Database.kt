package com.dream_warriors.hunger4none.data.realm

import io.realm.Realm
import io.realm.RealmObject

class Database
{
    fun <MODEL : RealmObject> select(type: Class<MODEL>, fields: HashMap<String, String> = hashMapOf()): MODEL?
    {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val model = realm.where(type)
        fields.forEach { model.equalTo(it.key, it.value) }
        val selection = model.findFirst()
        val data = if (selection != null) realm.copyFromRealm(selection)
        else null
        realm.commitTransaction()
        realm.close()
        return data
    }

    fun <MODEL : RealmObject> selectAll(type: Class<MODEL>, fields: HashMap<String, String> = hashMapOf()): ArrayList<MODEL>
    {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val model = realm.where(type)
        fields.forEach { model.equalTo(it.key, it.value) }
        val selection = model.findAll()
        val data = if (selection != null) ArrayList(realm.copyFromRealm(selection))
        else arrayListOf()
        realm.commitTransaction()
        realm.close()
        return data
    }

    fun <MODEL : RealmObject> insert(data: MODEL)
    {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.insertOrUpdate(data)
        realm.commitTransaction()
        realm.close()
    }

    fun <MODEL : RealmObject> insertAll(data: ArrayList<MODEL>)
    {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.insertOrUpdate(data)
        realm.commitTransaction()
        realm.close()
    }

    fun <MODEL : RealmObject> delete(type: Class<MODEL>, fields: HashMap<String, String> = hashMapOf())
    {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val data = realm.where(type)
        fields.forEach { data.equalTo(it.key, it.value) }
        data.findFirst()?.deleteFromRealm()
        realm.commitTransaction()
        realm.close()
    }

    fun <MODEL : RealmObject> deleteAll(type: Class<MODEL>, fields: HashMap<String, String> = hashMapOf())
    {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val data = realm.where(type)
        fields.forEach { data.equalTo(it.key, it.value) }
        data.findAll()?.deleteAllFromRealm()
        realm.commitTransaction()
        realm.close()
    }

    fun <MODEL : RealmObject> clear(type: Class<MODEL>)
    {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(type)
        realm.commitTransaction()
        realm.close()
    }

    fun clear()
    {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
        realm.close()
    }
}