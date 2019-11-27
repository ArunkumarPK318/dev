import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { MyapplivationSharedModule } from 'app/shared/shared.module';
import { MyapplivationCoreModule } from 'app/core/core.module';
import { MyapplivationAppRoutingModule } from './app-routing.module';
import { MyapplivationHomeModule } from './home/home.module';
import { MyapplivationEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    MyapplivationSharedModule,
    MyapplivationCoreModule,
    MyapplivationHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    MyapplivationEntityModule,
    MyapplivationAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class MyapplivationAppModule {}
