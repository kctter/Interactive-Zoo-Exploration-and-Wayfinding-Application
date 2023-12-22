package com.bennettzhang.zooseeker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


/**
 * A ViewModel class designed to store and manage UI-related data
 */
public class ZooDataViewModel extends AndroidViewModel {
     LiveData<List<Exhibit>> zooData;
    public final ZooDataDao zooDataDao;

    public ZooDataViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ZooDatabase db = ZooDatabase.getSingleton(context);
        zooDataDao = db.zooDataDao();
        ZooData.initZooData(context);
    }

    public LiveData<List<Exhibit>> getZooData() {
        if (zooData == null) {
            loadUsers();
        }

        return zooData;
    }

    private void loadUsers() {
        zooData = zooDataDao.getAllLive();
    }

    public void togglePlanned(Exhibit exhibit) {
        exhibit.planned = !exhibit.planned;
        ZooData.getVertexList().get(exhibit.id).planned = false;
        zooDataDao.update(exhibit);
        updateGroupExhibit(exhibit);
    }

    private void updateGroupExhibit(Exhibit exhibit){
        if(exhibit.hasGroup()){
            getZooData();
            boolean keep = false;
            for(Exhibit exhib : zooDataDao.getAll()) {
                if (exhib.hasGroup()) {
                    assert exhib.groupId != null;
                    if (exhib.groupId.equals(exhibit.groupId) && exhib.planned) {
                        keep = true;
                        break;
                    }
                }
            }
            if(!keep){
                removeGroup(ZooData.getVertexList().get(exhibit.groupId));
            } else {
                addPlanned(ZooData.getVertexList().get(exhibit.groupId));
            }
        }
    }

    public void addPlanned(Exhibit exhibit) {
        exhibit.planned = true;
        zooDataDao.update(exhibit);
    }

    public void removePlanned(Exhibit exhibit) {
        if(exhibit.isGroup()){
            return;
        }
        exhibit.planned = false;
        zooDataDao.update(exhibit);
        updateGroupExhibit(exhibit);
    }

    private void removeGroup(Exhibit exhibit) {
        exhibit.planned = false;
        zooDataDao.update(exhibit);
    }

    public void clearAll(List<Exhibit> exhibitList) {
        for (int i = 0; i < exhibitList.size(); i++) {
            removePlanned(exhibitList.get(i));
        }
    }

    /*
    public void updateText(TodoListItem todoListItem, String newText){
        todoListItem.text = newText;
        todoListItemDao.update(todoListItem);
    }

    public void createTodo(String text){
        int endOfListOrder = todoListItemDao.getOrderForAppend();
        TodoListItem newItem = new TodoListItem(text, false, endOfListOrder);
        todoListItemDao.insert(newItem);
    }

    public void deleteCompleted(TodoListItem todoListItem){
        todoListItemDao.delete(todoListItem);
    }
    */
}
