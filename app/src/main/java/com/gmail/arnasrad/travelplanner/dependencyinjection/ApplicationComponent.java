/*
 * *
 *  * Copyright (C) 2017 Ryan Kay Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.gmail.arnasrad.travelplanner.dependencyinjection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import com.gmail.arnasrad.travelplanner.create.CreateFragment;
import com.gmail.arnasrad.travelplanner.create.GoogleMapFragment;
import com.gmail.arnasrad.travelplanner.create.MainCreateFragment;
import com.gmail.arnasrad.travelplanner.create.MapFragment;
import com.gmail.arnasrad.travelplanner.detail.DetailFragment;
import com.gmail.arnasrad.travelplanner.list.ListFragment;
import com.gmail.arnasrad.travelplanner.login.LoginFragment;
import com.gmail.arnasrad.travelplanner.login.SignInFragment;


@Singleton
@Component(modules = {ApplicationModule.class, RoomModule.class})
public interface ApplicationComponent {

    void inject(ListFragment listFragment);
    void inject(MainCreateFragment mainCreateFragment);
    void inject(CreateFragment createFragment);
    //void inject(MapFragment mapFragment);
    void inject(GoogleMapFragment googleMapFragment);
    void inject(DetailFragment detailFragment);
    void inject(LoginFragment loginFragment);
    void inject(SignInFragment signInFragment);

    Application application();
}
