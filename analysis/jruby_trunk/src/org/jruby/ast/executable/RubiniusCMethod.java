/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2007 Ola Bini <ola.bini@gmail.com>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.ast.executable;

import org.jruby.RubyArray;
import org.jruby.RubyNumeric;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 */
public class RubiniusCMethod {
    public String name;
    public String file;
    public int locals; 
    public IRubyObject[] literals;
    public char[] code;
    public int required;

    public RubiniusCMethod(RubyArray obj) {
        code = obj.eltInternal(4).toString().toCharArray();
        name = obj.eltInternal(5).toString();
        file = obj.eltInternal(6).toString();
        locals = RubyNumeric.fix2int(obj.eltInternal(7));
        required = RubyNumeric.fix2int(obj.eltInternal(2));
        literals = toArray(obj.eltInternal(8));
    }

    private final IRubyObject[] toArray(IRubyObject oo) {
        if(oo.isNil()) {
            return new IRubyObject[0];
        }
        return (IRubyObject[])((RubyArray)oo).toArray(new IRubyObject[0]);
    }
}// RubiniusCMethod