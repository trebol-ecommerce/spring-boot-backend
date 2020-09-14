package cl.blm.newmarketing.store.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.store.jpa.entities.Permission;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Permission2GranthedAuthorityPojo
    implements Converter<Permission, SimpleGrantedAuthority> {

  @Override
  public SimpleGrantedAuthority convert(Permission source) {
    SimpleGrantedAuthority target = new SimpleGrantedAuthority(source.getCode());
    return target;
  }

}
