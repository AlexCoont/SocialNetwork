webpackJsonp([7],{Pvj0:function(t,i,e){"use strict";Object.defineProperty(i,"__esModule",{value:!0});var n=e("Dd8w"),s=e.n(n),a=e("NYxO"),o={name:"PushSidebar",props:{value:String},data:function(){return{list:["Все","Комментарии","Друзья"]}},methods:{changePushSidebar:function(t){this.$emit("change-push-sidebar",t)}}},c={render:function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"aside-filter"},[e("h2",{staticClass:"aside-filter__title"},[t._v("Оповещения")]),e("ul",{staticClass:"aside-filter__list"},t._l(t.list,function(i){return e("li",{key:i,staticClass:"aside-filter__item",class:{active:i===t.value},on:{click:function(e){return t.changePushSidebar(i)}}},[t._v(t._s(i))])}),0)])},staticRenderFns:[]},r=e("VU/8")(o,c,!1,null,null,null).exports,l=e("6nA3"),u={name:"PushBlock",props:{info:Object},computed:s()({},Object(a.c)("profile/notifications",["getNotificationsTextType"])),methods:{getRouteByNotification:l.a}},f={render:function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"push-block"},[e("div",{staticClass:"push__img"},[e("img",{attrs:{src:t.info.photo,alt:t.info.entity_author.first_name}})]),e("p",{staticClass:"push__content"},[e("router-link",{staticClass:"push__content-name",attrs:{to:t.getRouteByNotification(t.info)}},[t._v(t._s(t.info.entity_author.first_name+" "+t.info.entity_author.last_name)+"\n\n"+t._s(t.getNotificationsTextType(t.info.event_type)))]),e("span",{staticClass:"push__content-preview"},[t._v("«"+t._s(t.info.info)+"»")])],1),e("span",{staticClass:"push__time"},[t._v(t._s(t._f("moment")(t.info.sent_time,"from")))])])},staticRenderFns:[]};var _={name:"PagePush",components:{PushSidebar:r,PushBlock:e("VU/8")(u,f,!1,function(t){e("YhQD")},null,null).exports},data:function(){return{activeFilter:"Все"}},computed:s()({},Object(a.c)("profile/notifications",["getNotifications","getNotificationsLength"]),{filterNotifications:function(){switch(this.activeFilter){case"Все":return this.getNotifications;case"Комментарии":return this.getNotifications.filter(function(t){return"POST_COMMENT"===t.event_type||"COMMENT_COMMENT"===t.event_type});case"Друзья":return this.getNotifications.filter(function(t){return"FRIEND_REQUEST"===t.event_type})}}}),methods:s()({},Object(a.b)("profile/notifications",["apiNotifications","readNotifications"]),{onChangeFilter:function(t){this.activeFilter=t}}),beforeRouteEnter:function(t,i,e){e(function(t){t.apiNotifications(),t.readNotifications()})}},h={render:function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"page-push inner-page"},[t.getNotificationsLength>0?e("div",{staticClass:"inner-page__main"},t._l(t.filterNotifications,function(t){return e("push-block",{key:t.id,attrs:{info:t}})}),1):t._e(),e("div",{staticClass:"inner-page__aside"},[e("push-sidebar",{on:{"change-push-sidebar":t.onChangeFilter},model:{value:t.activeFilter,callback:function(i){t.activeFilter=i},expression:"activeFilter"}})],1)])},staticRenderFns:[]};var p=e("VU/8")(_,h,!1,function(t){e("m6Om")},null,null);i.default=p.exports},YhQD:function(t,i){},m6Om:function(t,i){}});
//# sourceMappingURL=7.b8113d368fe2fdfd7227.js.map