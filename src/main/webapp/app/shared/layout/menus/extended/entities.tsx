import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from '../menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/child">
      <Translate contentKey="global.menu.entities.child" />
    </MenuItem>
    {props.isAdmin && (
      <MenuItem icon="asterisk" to="/description">
        <Translate contentKey="global.menu.entities.description" />
      </MenuItem>
    )}
    {props.isAdmin && (
      <MenuItem icon="asterisk" to="/address">
        <Translate contentKey="global.menu.entities.address" />
      </MenuItem>
    )}
    {props.isAdmin && (
      <MenuItem icon="asterisk" to="/last-seen">
        <Translate contentKey="global.menu.entities.lastSeen" />
      </MenuItem>
    )}
    {props.isAdmin && (
      <MenuItem icon="asterisk" to="/contact-information">
        <Translate contentKey="global.menu.entities.contactInformation" />
      </MenuItem>
    )}
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
