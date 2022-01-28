import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LastSeenAddress from './last-seen-address';
import LastSeenAddressDetail from './last-seen-address-detail';
import LastSeenAddressUpdate from './last-seen-address-update';
import LastSeenAddressDeleteDialog from './last-seen-address-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LastSeenAddressUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LastSeenAddressUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LastSeenAddressDetail} />
      <ErrorBoundaryRoute path={match.url} component={LastSeenAddress} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LastSeenAddressDeleteDialog} />
  </>
);

export default Routes;
