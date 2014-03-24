/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012 gnosygnu@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package gplx.gfui; import gplx.*;
public class Mem_kit extends Gfui_kit_base {
	@Override public byte Tid() {return Gfui_kit_.Mem_tid;}
	@Override public String Key() {return "mem";}
	@Override public GxwElemFactory_base Factory() {return factory;} private GxwElemFactory_cls_mock factory = new GxwElemFactory_cls_mock();
	@Override protected Gxw_html New_html_impl() {return new Mem_html();}
	@Override protected GxwElem New_btn_impl() {return factory.control_();}
	@Override public ImageAdp New_img_load(Io_url url) {return ImageAdp_null._;}
        public static final Mem_kit _ = new Mem_kit(); Mem_kit() {}
}
