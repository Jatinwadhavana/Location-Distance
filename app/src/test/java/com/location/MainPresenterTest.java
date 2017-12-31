package com.location;

import com.location.presenter.MainPresenter;
import com.location.views.MainView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    private MainView mainView;

    @Mock
    private MainPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new MainPresenter(mainView);
    }

    @Test
    public void shouldShowMessageIfGpsOff() throws Exception {
        when(mainView.checkGPS()).thenReturn(false);
        mainView.checkGPS();
    }
    @Test
    public void shouldShowMessageIfGpsOn() throws Exception {
        when(mainView.checkGPS()).thenReturn(true);
        mainView.checkGPS();
    }
}
