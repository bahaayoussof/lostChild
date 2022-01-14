import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LastSeen from './last-seen';
import LastSeenDetail from './last-seen-detail';
import LastSeenUpdate from './last-seen-update';
import LastSeenDeleteDialog from './last-seen-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LastSeenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LastSeenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LastSeenDetail} />
      <ErrorBoundaryRoute path={match.url} component={LastSeen} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LastSeenDeleteDialog} />
  </>
);

export default Routes;
